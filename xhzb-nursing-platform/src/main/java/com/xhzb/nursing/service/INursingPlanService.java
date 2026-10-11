package com.xhzb.nursing.service;

import java.util.List;
import com.xhzb.nursing.domain.NursingPlan;
import com.xhzb.nursing.dto.NursingPlanDto;

/**
 * 护理计划Service接口
 * 
 * @author xujingbo
 * @date 2026-10-11
 */
public interface INursingPlanService 
{
    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    public NursingPlan selectNursingPlanById(Long id);

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    /**
     * 查询所有护理计划（不分页）
     * 
     * @return 护理计划集合
     */
    public List<NursingPlan> selectNursingPlanAll();

    /**
     * 新增护理计划
     *
     * @param dto 护理计划
     * @return 结果
     */
    public int insertNursingPlan(NursingPlanDto dto);

    /**
     * 修改护理计划
     * 说明：护理项目列表不为空时，会先删除中间表原数据再写入新数据；为空时只修改护理计划
     * 
     * @param dto 护理计划（含护理项目关联列表）
     * @return 结果
     */
    public int updateNursingPlan(NursingPlanDto dto);

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的护理计划主键集合
     * @return 结果
     */
    public int deleteNursingPlanByIds(Long[] ids);

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    public int deleteNursingPlanById(Long id);
}
