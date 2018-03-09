package jiangsu.tbkt.teacher.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import org.xwalk.core.XWalkView;

/**
 * Created by 梁家霖 on 2017/3/20.
 */
public class MyXwalkview extends XWalkView{
    public MyXwalkview(Context context) {
        super(context);
    }

    public MyXwalkview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyXwalkview(Context context, Activity activity) {
        super(context, activity);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return false;
//        }
//        return false;
//    }

}
