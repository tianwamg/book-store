package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 敏感词
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sensitive")
public class Sensitive implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 敏感词
     */
    private String word;

    /**
     * 状态 1:正常 -1:删除
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
