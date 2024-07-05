package com.cn.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 书籍库
 * </p>
 *
 * @author Ranger
 * @since 2024-05-03
 */
@Data
@TableName("book_info")
@EqualsAndHashCode(callSuper = false)
public class BookInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pullId;

    /**
     * userid
     */
    private Long userId;

    /**
     * 名称
     */
    private String title;

    /**
     * 图片
     */
    private String img;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * ISBN
     */
    private String isbn;

    /**
     * 店铺id
     */
    private Integer shopid;

    /**
     * 商品id
     */
    private String itemid;

    /**
     * 质量
     */
    private String quality;

    /**
     * 说明
     */
    private String remark;

    /**
     * 1:已拉取 2:已上架
     */
    private Integer status;

    /**
     * 额外信息
     */
    private String extra;

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
    private Integer isDelete;


}
