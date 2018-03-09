package jiangsu.tbkt.teacher.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Description:
 * @FileName: 跑马灯效果的textivew
 * @Package com.tbkt.teacher_eng.widgets
 * @Author zhangerbin
 * @Date 2015/7/10
 * @Version V1.0
 */
public class MarqueeTextView extends TextView {

    public MarqueeTextView(Context context) {

        super(context);

        // TODO Auto-generated constructor stub
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

        // TODO Auto-generated constructor stub
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean isFocused() {

        return true;

    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

        // TODO Auto-generated method stub
        // fobid call parent constructor
        // super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

}
