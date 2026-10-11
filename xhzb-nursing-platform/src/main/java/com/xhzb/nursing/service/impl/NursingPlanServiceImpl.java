package com.xhzb.nursing.service.impl;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.xhzb.common.utils.DateUtils;
import com.xhzb.nursing.dto.NursingPlanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.xhzb.nursing.mapper.NursingProjectPlanMapper;
import com.xhzb.nursing.mapper.NursingPlanMapper;
import com.xhzb.nursing.domain.NursingPlan;
import com.xhzb.nursing.domain.NursingProjectPlan;
import com.xhzb.nursing.service.INursingPlanService;

/**
 * 护理计划Service业务层处理
 * 
 * @author xujingbo
 * @date 2026-10-11
 */
@Service
public class NursingPlanServiceImpl implements INursingPlanService 
{
    @Autowired
    private NursingPlanMapper nursingPlanMapper;

    @Autowired
    private NursingProjectPlanMapper nursingProjectPlanMapper;

    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    @Override
    public NursingPlan selectNursingPlanById(Long id)
    {
        NursingPlan nursingPlan = nursingPlanMapper.selectNursingPlanById(id);
        if (nursingPlan != null)
        {
            // 查询护理计划下关联的护理项目，供前端编辑回显
            NursingProjectPlan query = new NursingProjectPlan();
            query.setPlanId(id);
            nursingPlan.setProjectPlans(nursingProjectPlanMapper.selectNursingProjectPlanList(query));
        }
        return nursingPlan;
    }

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划
     */
    @Override
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan)
    {
        return nursingPlanMapper.selectNursingPlanList(nursingPlan);
    }

    /**
     * 查询所有护理计划（不分页）
     * 说明：不传查询条件即查询全部护理计划，供下拉/选择使用
     * 
     * @return 护理计划集合
     */
    @Override
    public List<NursingPlan> selectNursingPlanAll()
    {
        return nursingPlanMapper.selectNursingPlanList(new NursingPlan());
    }


    /**
     * 新增护理计划
     *
     * @param dto 护理计划
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertNursingPlan(NursingPlanDto dto)
    {
        //1. 复制对象：dto -> pojo (注意复制对象属性成功的标准：两个对象的属性名和类型必须一样)
        NursingPlan nursingPlan = BeanUtil.toBean(dto, NursingPlan.class);

        //2. 保存护理计划
        nursingPlan.setCreateTime(DateUtils.getNowDate());
        nursingPlanMapper.insertNursingPlan(nursingPlan);

        //3. 批量保存护理项目与护理计划中间表数据（关系数据）
        if (CollUtil.isNotEmpty(dto.getProjectPlans())) {
            dto.getProjectPlans().forEach(pp->{
                pp.setCreateTime(DateUtils.getNowDate());
                pp.setPlanId(nursingPlan.getId()); //设置护理计划ID
            });
            int rows = nursingProjectPlanMapper.insertNursingProjectPlanBatch(dto.getProjectPlans());
            return rows;
        }

        return 0;
    }

    /**
     * 修改护理计划
     * 说明：dto 属性拷贝到新的 NursingPlan 对象；护理项目列表为空时只修改护理计划，
     *      不为空时先根据护理计划id删除中间表数据，再批量写入新的关联数据
     * 
     * @param dto 护理计划（含护理项目关联列表）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNursingPlan(NursingPlanDto dto)
    {
        // 1. 复制对象：dto -> pojo（属性名与类型一致时才会复制成功）
        NursingPlan nursingPlan = BeanUtil.toBean(dto, NursingPlan.class);
        nursingPlan.setUpdateTime(DateUtils.getNowDate());

        // 2. 护理项目列表不为空：先删除中间表原数据，再批量写入新数据（整体覆盖）
        if (CollUtil.isNotEmpty(dto.getProjectPlans()))
        {
            nursingProjectPlanMapper.deleteNursingProjectPlanByPlanId(nursingPlan.getId());
            dto.getProjectPlans().forEach(projectPlan -> {
                projectPlan.setId(null); // 中间表主键由数据库重新生成
                projectPlan.setPlanId(nursingPlan.getId());
                projectPlan.setCreateTime(DateUtils.getNowDate());
            });
            nursingProjectPlanMapper.insertNursingProjectPlanBatch(dto.getProjectPlans());
        }

        // 3. 更新护理计划主表
        return nursingPlanMapper.updateNursingPlan(nursingPlan);
    }

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的护理计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNursingPlanByIds(Long[] ids)
    {
        for (Long id : ids)
        {
            nursingProjectPlanMapper.deleteNursingProjectPlanByPlanId(id);
        }
        return nursingPlanMapper.deleteNursingPlanByIds(ids);
    }

    /**
     * 删除护理计划信息
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteNursingPlanById(Long id)
    {
        nursingProjectPlanMapper.deleteNursingProjectPlanByPlanId(id);
        return nursingPlanMapper.deleteNursingPlanById(id);
    }
}
