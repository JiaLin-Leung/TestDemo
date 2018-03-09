package jiangsu.tbkt.teacher.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * @Module :
 * @Comments : PicassoUtils
 * @Author : KnightOneAdmin
 * @CreateDate : 16/9/11
 * @ModifiedBy : KnightOneAdmin
 * @ModifiedDate: 下午3:36
 * @Modified : PicassoUtils
 */
public class PicassoUtils {
    /**
     * 设置图片
     *
     * @param context
     * @param url
     * @param img
     */
    public static void setAvatarImg(Context context, String url, ImageView img) {

        Glide.with(context)
                .load(url)
                .priority(Priority.LOW)
//                syw 跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                syw 跳过内存缓存
                .skipMemoryCache(true)
                .centerCrop()
                .into(img);

    }
}
