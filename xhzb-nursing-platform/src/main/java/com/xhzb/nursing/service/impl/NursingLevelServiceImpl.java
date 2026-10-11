package com.xhzb.nursing.service.impl;

import java.util.List;
import com.xhzb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhzb.nursing.mapper.NursingLevelMapper;
import com.xhzb.nursing.domain.NursingLevel;
import com.xhzb.nursing.service.INursingLevelService;

/**
 * 护理等级Service业务层处理
 * 
 * @author xujingbo
 * @date 2026-10-11
 */
@Service
public class NursingLevelServiceImpl implements INursingLevelService 
{
    @Autowired
    private NursingLevelMapper nursingLevelMapper;

    /**
     * 查询护理等级
     * 
     * @param id 护理等级主键
     * @return 护理等级
     */
    @Override
    public NursingLevel selectNursingLevelById(Long id)
    {
        return nursingLevelMapper.selectNursingLevelById(id);
    }

    /**
     * 查询护理等级列表
     * 
     * @param nursingLevel 护理等级
     * @return 护理等级
     */
    @Override
    public List<NursingLevel> selectNursingLevelList(NursingLevel nursingLevel)
    {
        return nursingLevelMapper.selectNursingLevelList(nursingLevel);
    }

    /**
     * 查询所有护理等级（不分页）
     * 说明：不传查询条件即查询全部护理等级，供下拉/选择使用
     * 
     * @return 护理等级集合
     */
    @Override
    public List<NursingLevel> selectNursingLevelAll()
    {
        return nursingLevelMapper.selectNursingLevelList(new NursingLevel());
    }

    /**
     * 新增护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int insertNursingLevel(NursingLevel nursingLevel)
    {
        nursingLevel.setCreateTime(DateUtils.getNowDate());
        return nursingLevelMapper.insertNursingLevel(nursingLevel);
    }

    /**
     * 修改护理等级
     * 
     * @param nursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int updateNursingLevel(NursingLevel nursingLevel)
    {
        nursingLevel.setUpdateTime(DateUtils.getNowDate());
        return nursingLevelMapper.updateNursingLevel(nursingLevel);
    }

    /**
     * 批量删除护理等级
     * 
     * @param ids 需要删除的护理等级主键
     * @return 结果
     */
    @Override
    public int deleteNursingLevelByIds(Long[] ids)
    {
        return nursingLevelMapper.deleteNursingLevelByIds(ids);
    }

    /**
     * 删除护理等级信息
     * 
     * @param id 护理等级主键
     * @return 结果
     */
    @Override
    public int deleteNursingLevelById(Long id)
    {
        return nursingLevelMapper.deleteNursingLevelById(id);
    }
}
