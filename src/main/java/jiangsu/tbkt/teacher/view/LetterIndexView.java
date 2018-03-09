package jiangsu.tbkt.teacher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * �Զ�������view
 * @author song
 */
public class LetterIndexView extends View {
    //��ǰ��ָ��������λ��
    private int choosedPosition = -1;
    //�����ֵĻ���
    private Paint paint;
    //�ұߵ���������
    private String[] letters = new String[]{"#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    //ҳ���������TextView��������ʾ��ָ��ǰ��������λ�õ��ı�
    private TextView textViewDialog;
    //�ӿڱ������ýӿ���Ҫ����ʵ�ֵ���ָ���ұߵĻ����ؼ��ϻ���ʱListView�ܹ����Ź���
    private UpdateListView updateListView;

    public LetterIndexView(Context context) {
        this(context, null);
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(32);
    }

    public void setTextViewDialog(TextView textViewDialog) {
        this.textViewDialog = textViewDialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int perTextHeight = getHeight() / letters.length;
        for (int i = 0; i < letters.length; i++) {
            if (i == choosedPosition) {
                paint.setColor(Color.parseColor("#12B7F5"));
            } else {
                paint.setColor(Color.parseColor("#AAAAAA"));
            }
            canvas.drawText(letters[i], (getWidth() - paint.measureText(letters[i])) / 2, (i + 1) * perTextHeight, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int perTextHeight = getHeight() / letters.length;
        float y = event.getY();
        int currentPosition = (int) (y / perTextHeight);
        //syw 下标越界异常和滑动到屏幕外取消显示
        if (currentPosition>=letters.length||currentPosition<0){
            setBackgroundColor(Color.TRANSPARENT);
            if (textViewDialog != null) {
                textViewDialog.setVisibility(View.GONE);
            }
            return true;
        }
        String letter = letters[currentPosition];

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                if (textViewDialog != null) {
                    textViewDialog.setVisibility(View.GONE);
                }
                break;

            default:
                setBackgroundColor(Color.parseColor("#cccccc"));
                if (currentPosition > -1 && currentPosition < letters.length) {
                    if (textViewDialog != null) {
                        textViewDialog.setVisibility(View.VISIBLE);
                        textViewDialog.setText(letter);
                    }
                    if (updateListView != null) {
                        updateListView.updateListView(letter);
                    }
                    choosedPosition = currentPosition;
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setUpdateListView(UpdateListView updateListView) {
        this.updateListView = updateListView;
    }

    public interface UpdateListView {
        public void updateListView(String currentChar);
    }

    public void updateLetterIndexView(int currentChar) {
        for (int i = 0; i < letters.length; i++) {
            if (currentChar == letters[i].charAt(0)) {
                choosedPosition = i;
                invalidate();
                break;
            }
        }
    }
}
