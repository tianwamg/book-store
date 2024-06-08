package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 水印
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Waterprint implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 水印名称
     */
    private String name;

    /**
     * 图片说明
     */
    private String remark;

    /**
     * 位置
     */
    private String locate;

    /**
     * 地址
     */
    private String url;

    /**
     * 状态 0:未启用，1:启用
     */

    private Integer status;

    private Long userId;

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



}
