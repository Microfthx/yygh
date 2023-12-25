package jw.hospital.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jw.hospital.yygh.common.exception.HospitalException;
import jw.hospital.yygh.common.result.ResultCodeEnum;
import jw.hospital.yygh.hosp.repository.DepartmentRespository;
import jw.hospital.yygh.hosp.repository.ScheduleRespository;
import jw.hospital.yygh.hosp.service.DepartmentService;
import jw.hospital.yygh.hosp.service.HospitalService;
import jw.hospital.yygh.hosp.service.ScheduleService;
import jw.hospital.yygh.model.hosp.BookingRule;
import jw.hospital.yygh.model.hosp.Department;
import jw.hospital.yygh.model.hosp.Hospital;
import jw.hospital.yygh.model.hosp.Schedule;
import jw.hospital.yygh.vo.hosp.BookingScheduleRuleVo;
import jw.hospital.yygh.vo.hosp.DepartmentQueryVo;
import jw.hospital.yygh.vo.hosp.ScheduleOrderVo;
import jw.hospital.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRespository scheduleRespository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BaseMapper baseMapper;

    @Override
    public void save(Map<String, Object> paramMap) {
        //paramMap 转换
        String paramMapString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(paramMapString, Schedule.class);

        Schedule scheduleExist = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (scheduleExist != null) {
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleRespository.save(scheduleExist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRespository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
//        paramMap 转换
//        String paramMapString = JSONObject.toJSONString(paramMap);
//        Department department = JSONObject.parseObject(paramMapString,Department.class);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase(true)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Schedule> example = Example.of(schedule, matcher);
        return scheduleRespository.findAll(example, pageable);
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {

        Schedule schedule = scheduleRespository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRespository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        Map<String, Object> map = new HashMap<>();
//        scheduleRespository.getSchedulesByHoscodeAndDepcode(hoscode,depcode);
        //基于 mongoTemplate 做到以下操作
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),//匹配条件
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        //统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
                //排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                //4.实现分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        //调用方法,最终执行
        AggregationResults<BookingScheduleRuleVo> aggResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResults.getMappedResults();

        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResults =
                mongoTemplate.aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResults.getMappedResults().size();

        for (BookingScheduleRuleVo bookingScheduleRuleVo : bookingScheduleRuleVoList) {
            Date workDate = bookingScheduleRuleVo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);
        }

        //设置最终数据,进行返回


        map.put("bookingScheduleRuleVoList", bookingScheduleRuleVoList);
        map.put("total", total);

        //获取医院名称
        String hosName = hospitalService.getHospName(hoscode);
        Map<String, Object> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        map.put("baseMap", baseMap);
        return map;
    }

    @Override
    public List<Schedule> getRuleScheduleDetail(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleList = scheduleRespository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDateTime());

        scheduleList.forEach(this::packageSchedule);
        return scheduleList;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        Map<String, Object> result = new HashMap<>();
        //获取预约规则
        //根据医院编号获取预约规则
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if (hospital == null) {
            throw new HospitalException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        IPage iPage = this.getListDate(page, limit, bookingRule);

        List<Date> dateList = iPage.getRecords();

        //获取可预约日期里面科室的剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode)
                .and("workDate").in(dateList);

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                .count().as("docCount")
                .sum("availableNumber").as("availableNumber")
                .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregateResult =
                mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleRuleVoList = aggregateResult.getMappedResults();

        Map<Date, BookingScheduleRuleVo> scheduleRuleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleRuleVoList)){
            scheduleRuleVoMap = scheduleRuleVoList.stream()
                    .collect(
                            Collectors.toMap(BookingScheduleRuleVo::getWorkDate,
                                    BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }

        //获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        int len=dateList.size();
        for (int i = 0; i < len; i++) {
            Date date = dateList.get(i);
            //从map集合根据key日期获取value值
            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleRuleVoMap.get(date);
            //如果当天没有排版医生
            if(bookingScheduleRuleVo == null){
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                bookingScheduleRuleVo.setDocCount(0);
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);

            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if(i == len-1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间， 不能预约
            if(i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);

        }
//可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());

        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getHospName(hoscode));
        //科室
        Department department =departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
//月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
//放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
//停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;
    }

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
//    @Override
//    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
//        //根据参数查询mongodb
//        List<Schedule> scheduleList =
//                scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate).toDate());
//        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
//        scheduleList.stream().forEach(item->{
//            this.packageSchedule(item);
//        });
//        return scheduleList;
//    }

    private IPage getListDate(Integer page, Integer limit, BookingRule bookingRule) {
        //获取当天放号时间 年 月 日 小时 分钟
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //获取预约周期
        Integer cycle = bookingRule.getCycle();
        //如果当天放号时间已经过去,预约周期从后一天开始计算,周期+1
        if(releaseTime.isBeforeNow()) {
            cycle+=1;
        }
        //获取可预约所有日期,最后一天显示即将放号
        List<Date> dateList = new ArrayList<>();
        for (Integer i = 0; i < cycle; i++) {
            DateTime curDateTime = new DateTime().plusDays(i);
            String dateString = curDateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(dateString).toDate());
        }
        //因为预约周期不同,每页显示日期最多7天数据,超过7天分页
        List<Date> pageDateList = new ArrayList<>();
        int start = (page-1)*limit;
        int end = (page-1)*limit+limit;
        //
        if(end>dateList.size()){
            end=dateList.size();
        }
        for(int i=start;i<end;i++){
            pageDateList.add(dateList.get(i));
        }
        //
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page,7,dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd")+" "+timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    private Schedule packageSchedule(Schedule item) {
        //设置医院名称
        item.getParam().put("hosname",hospitalService.getHospName(item.getHoscode()));
        //设置科室名称
        item.getParam().put("depname",departmentService.getDepName(item.getHoscode(),item.getDepcode()));
        //设置日期
        item.getParam().put("dayOfWeek",this.getDayOfWeek(new DateTime(item.getWorkDate())));

        return item;
    }

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {
        //根据参数查询mongodb
        List<Schedule> scheduleList =
                scheduleRespository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate));
        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        scheduleList.stream().forEach(item->{
            this.packageSchedule(item);
        });
        return scheduleList;
    }

    @Override
    public Schedule getScheduleId(String scheduleId) {
        Schedule schedule = scheduleRespository.findById(scheduleId).get();
        schedule = this.packageSchedule(schedule);
        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Object id = baseMapper.selectById(scheduleId);
        return null;
    }

    /**
    *根据日期获取周几数据
    * @param dateTime
    * @return
     * */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = " ";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
                break;
        }
        return dayOfWeek;
    }
}
