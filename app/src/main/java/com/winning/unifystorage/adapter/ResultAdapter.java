package com.winning.unifystorage.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.winning.unifystorage.R;
import com.winning.unifystorage.model.User;

/**
 * 2018/12/5
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class ResultAdapter extends BaseQuickAdapter<User,BaseViewHolder> {
    public ResultAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        StringBuffer result = new StringBuffer();
        result.append(item.toString()+"\n\n");
        result.append(item.getDog()+"\n\n");
        for (int i = 0;i < item.getCats().size();i++){
            result.append(item.getCats().get(i).toString()+"\n");
        }
        helper.setText(R.id.tvText,result.toString());
    }
}
