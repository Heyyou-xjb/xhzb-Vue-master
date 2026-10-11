package com.xhzb.nursing.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.xhzb.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xhzb.nursing.mapper.NursingProjectMapper;
import com.xhzb.nursing.domain.NursingProject;
import com.xhzb.nursing.domain.vo.ProjectOptionVo;
import com.xhzb.nursing.service.INursingProjectService;

/**
 * 护理项目Service业务层处理
 * 
 * @author xujingbo
 * @date 2026-10-10
 */
@Service
public class NursingProjectServiceImpl implements INursingProjectService 
{
    @Autowired
    private NursingProjectMapper nursingProjectMapper;

    /**
     * 查询护理项目
     * 
     * @param id 护理项目主键
     * @return 护理项目
     */
    @Override
    public NursingProject selectNursingProjectById(Long id)
    {
        return nursingProjectMapper.selectNursingProjectById(id);
    }

    /**
     * 查询护理项目列表
     * 
     * @param nursingProject 护理项目
     * @return 护理项目
     */
    @Override
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject)
    {
        return nursingProjectMapper.selectNursingProjectList(nursingProject);
    }

    /**
     * 查询所有护理项目（下拉选项）
     * 说明：不传查询条件即查询全部护理项目，再在 Service 层组装为 label/value 结构
     * 
     * @return 护理项目选项集合
     */
    @Override
    public List<ProjectOptionVo> selectProjectOptions()
    {
        List<NursingProject> nursingProjects = nursingProjectMapper.selectNursingProjectList(new NursingProject());
        List<ProjectOptionVo> options = new ArrayList<>(nursingProjects.size());
        for (NursingProject nursingProject : nursingProjects)
        {
            options.add(new ProjectOptionVo(nursingProject.getName(), String.valueOf(nursingProject.getId())));
        }
        return options;
    }

    /**
     * 新增护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int insertNursingProject(NursingProject nursingProject)
    {
        nursingProject.setCreateTime(DateUtils.getNowDate());
        return nursingProjectMapper.insertNursingProject(nursingProject);
    }

    /**
     * 修改护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    @Override
    public int updateNursingProject(NursingProject nursingProject)
    {
        nursingProject.setUpdateTime(DateUtils.getNowDate());
        return nursingProjectMapper.updateNursingProject(nursingProject);
    }

    /**
     * 批量删除护理项目
     * 
     * @param ids 需要删除的护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingProjectByIds(Long[] ids)
    {
        return nursingProjectMapper.deleteNursingProjectByIds(ids);
    }

    /**
     * 删除护理项目信息
     * 
     * @param id 护理项目主键
     * @return 结果
     */
    @Override
    public int deleteNursingProjectById(Long id)
    {
        return nursingProjectMapper.deleteNursingProjectById(id);
    }
}
