package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 价格配置
 * </p>
 *
 * @author Ranger
 * @since 2024-04-02
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Price implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 档位名称
     */
    private String name;

    /**
     * 档位说明
     */
    private String remark;

    /**
     * 开始价格
     */
    private BigDecimal startPrice;

    /**
     * 结束价格
     */
    private BigDecimal endPrice;

    /**
     * 增加价格
     */
    private BigDecimal addPrice;

    /**
     * 操作符
     */
    private String simbol;

    /**
     * 操作
     */
    private String operate;

    /**
     * 状态 0:未启用，1:启用
     */

    private Integer status;

    @TableLogic(delval = "-1",value = "1")
    private Integer isDelete;

    private Long userId;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 修改时间
     */
    private Date mtime;


}
