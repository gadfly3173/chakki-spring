package vip.gadfly.chakkispring.common.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 简单分页模型
 * mybatis-plus的默认分页实现，起始页都是1，为了与其它端保持一致，故重写了Page，起始页从 0 开始
 *
 * @author hubin
 * @since 2018-06-09
 */
public class Page<T> extends com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> {

    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 该构造方法使得 current 总为 0
     */
    public Page() {
        super.setCurrent(0);
    }

    public Page(int current, int size) {
        this(current, size, 0);
    }

    public Page(int current, int size, int total) {
        this(current, size, total, true);
    }

    public Page(int current, int size, boolean isSearchCount) {
        this(current, size, 0, isSearchCount);
    }

    /**
     * 该构造方法将小于 0 的 current 置为 0
     *
     * @param current       当前页
     * @param size          每页显示条数，默认 10
     * @param total         总数
     * @param isSearchCount 是否进行 count 查询
     */
    public Page(int current, int size, int total, boolean isSearchCount) {
        super(current, size, total, isSearchCount);

        if (current < 0) {
            current = 0;
        }
        super.setCurrent(current);
    }

    @Override
    public boolean hasPrevious() {
        return super.getCurrent() > 0;
    }

    @Override
    public boolean hasNext() {
        return super.getCurrent() + 1 < this.getPages();
    }

    /**
     * 重写计算偏移量，将分页从第 0 开始
     *
     * @return 偏移量
     */
    @Override
    public long offset() {
        return getCurrent() > 0 ? super.getCurrent() * getSize() : 0;
    }
}