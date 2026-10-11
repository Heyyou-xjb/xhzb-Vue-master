package com.xhzb.nursing.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 护理项目下拉选项对象（label/value 结构）
 *
 * @author xujingbo
 * @date 2026-10-11
 */
public class ProjectOptionVo
{
    /** 下拉显示文本（护理项目名称） */
    private String label;

    /** 下拉选中值（护理项目ID，字符串形式） */
    private String value;

    public ProjectOptionVo()
    {
    }

    public ProjectOptionVo(String label, String value)
    {
        this.label = label;
        this.value = value;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("label", getLabel())
            .append("value", getValue())
            .toString();
    }
}
