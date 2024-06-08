package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Ranger
 * @since 2024-04-06
 */
@Builder
@TableName("book_pull")
@Data
@EqualsAndHashCode(callSuper = false)
public class BookPull implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * userid
     */
    private Long userId;

    /**
     * -1:已取消 0:未启用，1:进行中 2:已完成
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

    /**
     * 1:正常 -1:删除
     */
    @TableLogic(delval = "-1",value = "1")
    private Integer isDelete;


}
