package com.cn.domain;

import com.baomidou.mybatisplus.annotation.*;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 图书分类
 * </p>
 *
 * @author Ranger
 * @since 2024-03-30
 */
@TableName("book_category")
@Data
@EqualsAndHashCode(callSuper = false)
public class BookCategory implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 类名
     */
    private String name;

    /**
     * 分类
     */
    private String category;

    /**
     * 父id
     */
    private Integer pid;

    /**
     * 状态 0:未启用，1:启用：
     */
    private Integer status;

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
    private List<BookCategory> subList;

}
