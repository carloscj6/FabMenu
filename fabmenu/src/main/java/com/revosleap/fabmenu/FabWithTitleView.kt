package com.revosleap.fabmenu


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * View that contains mini fab button and its title.
 */
class FabWithTitleView
/**
 * Constructors.
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var mTitle: TextView? = null
    /**
     * @return This method returns mini fab in action menu.
     */
    var fab: FloatingActionButton? = null
        private set
    /**
     * @return This method returns mini fab titles background card.
     */
    var card: CardView? = null
        private set
    private var isTitleEnable: Boolean = false

    /**
     * @return true if button has title,false otherwise.
     */
    /**
     * Enables or disables title of button.
     */
    var titleEnable: Boolean
        get() = isTitleEnable
        set(enable) {
            card!!.visibility = if (enable) View.VISIBLE else View.INVISIBLE
        }


    init {
        init(context, attrs)
    }


    /**
     * Init custom attributes.
     *
     * @param context context.
     * @param attrs   attributes.
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        init(context)
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.FabWithTitleView, 0, 0
        )

        val titleText = a.getString(R.styleable.FabWithTitleView_fab_title)
        mTitle!!.text = titleText

        val fabColor = ContextCompat.getColor(context, R.color.colorPrimary)
        fab!!.backgroundTintList = ColorStateList.valueOf(fabColor)
        a.recycle()
    }

    private fun init(context: Context) {
        val rootView = View.inflate(context, R.layout.fab_with_label, this)

        fab = rootView.findViewById<View>(R.id.mini_fab) as FloatingActionButton
        mTitle = rootView.findViewById<View>(R.id.title_text) as TextView
        card = rootView.findViewById<View>(R.id.title_card) as CardView

        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            FabWithOptions.pxToDp(context, 56)
        )
        layoutParams.gravity = Gravity.END
        setLayoutParams(layoutParams)
        orientation = LinearLayout.HORIZONTAL
        clipChildren = false
        clipToPadding = false
    }

    /**
     * Sets mini fab drawable.
     *
     * @param mDrawable drawable to set.
     */
    fun setFabIcon(mDrawable: Drawable) {
        fab!!.setImageDrawable(mDrawable)
    }

    /**
     * Sets mini fab title․
     *
     * @param sequence title to set.
     */
    fun setMiniFabTitle(sequence: CharSequence) {
        if (!TextUtils.isEmpty(sequence)) {
            mTitle!!.text = sequence
            isTitleEnable = true
        } else {
            titleEnable = false
            isTitleEnable = false
        }
    }

    /**
     * Sets mini fab color in floating action menu.
     *
     * @param color color to set.
     */
    fun setMiniFabColor(color: Int) {
        fab!!.backgroundTintList = ColorStateList.valueOf(color)
    }

}

