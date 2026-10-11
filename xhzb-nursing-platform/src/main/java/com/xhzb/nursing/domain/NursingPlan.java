package com.xhzb.nursing.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.xhzb.common.annotation.Excel;
import com.xhzb.common.core.domain.BaseEntity;

/**
 * 护理计划对象 nursing_plan
 * 
 * @author xujingbo
 * @date 2026-10-11
 */
public class NursingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer sortNo;

    /** 名称 */
    @Excel(name = "名称")
    private String planName;

    /** 状态 0禁用 1启用 */
    @Excel(name = "状态 0禁用 1启用")
    private Integer status;

    /** 护理项目关联列表（护理计划下的护理项目） */
    private List<NursingProjectPlan> projectPlans;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSortNo(Integer sortNo) 
    {
        this.sortNo = sortNo;
    }

    public Integer getSortNo() 
    {
        return sortNo;
    }

    public void setPlanName(String planName) 
    {
        this.planName = planName;
    }

    public String getPlanName() 
    {
        return planName;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public List<NursingProjectPlan> getProjectPlans()
    {
        return projectPlans;
    }

    public void setProjectPlans(List<NursingProjectPlan> projectPlans)
    {
        this.projectPlans = projectPlans;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sortNo", getSortNo())
            .append("planName", getPlanName())
            .append("status", getStatus())
            .append("projectPlans", getProjectPlans())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .toString();
    }
}
