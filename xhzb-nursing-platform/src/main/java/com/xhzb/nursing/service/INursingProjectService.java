package com.xhzb.nursing.service;

import java.util.List;
import com.xhzb.nursing.domain.NursingProject;
import com.xhzb.nursing.domain.vo.ProjectOptionVo;

/**
 * 护理项目Service接口
 * 
 * @author xujingbo
 * @date 2026-10-10
 */
public interface INursingProjectService 
{
    /**
     * 查询护理项目
     * 
     * @param id 护理项目主键
     * @return 护理项目
     */
    public NursingProject selectNursingProjectById(Long id);

    /**
     * 查询护理项目列表
     * 
     * @param nursingProject 护理项目
     * @return 护理项目集合
     */
    public List<NursingProject> selectNursingProjectList(NursingProject nursingProject);

    /**
     * 查询所有护理项目（下拉选项，label 为项目名称，value 为项目id）
     * 
     * @return 护理项目选项集合
     */
    public List<ProjectOptionVo> selectProjectOptions();

    /**
     * 新增护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    public int insertNursingProject(NursingProject nursingProject);

    /**
     * 修改护理项目
     * 
     * @param nursingProject 护理项目
     * @return 结果
     */
    public int updateNursingProject(NursingProject nursingProject);

    /**
     * 批量删除护理项目
     * 
     * @param ids 需要删除的护理项目主键集合
     * @return 结果
     */
    public int deleteNursingProjectByIds(Long[] ids);

    /**
     * 删除护理项目信息
     * 
     * @param id 护理项目主键
     * @return 结果
     */
    public int deleteNursingProjectById(Long id);
}
