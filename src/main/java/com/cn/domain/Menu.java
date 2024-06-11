package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Ranger
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 状态 1:正常
     */
    private Integer status;

    /**
     * 父id
     */
    private Integer pid;

    /**
     * 路径
     */
    private String route;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 修改时间
     */
    private Date mtime;

    @TableLogic(delval = "-1",value = "1")
    private Integer isDelete;

    @TableField(exist = false)
    private List<Menu> subList;

}
