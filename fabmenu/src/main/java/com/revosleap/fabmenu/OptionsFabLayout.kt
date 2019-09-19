package com.revosleap.fabmenu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

class OptionsFabLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    RelativeLayout(context, attrs) {

    private var mFabWithOptions: FabWithOptions? = null
    lateinit var mRootView: FrameLayout
    private var bgColor: Int = 0


    /**
     * @return returns true if menu opened,false otherwise.
     */
    val isOptionsMenuOpened: Boolean
        get() = mFabWithOptions!!.isMiniFabsOpened

    init {
        init(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }


    private fun init(context: Context) {
        mRootView = FrameLayout(context)
        mRootView.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(mRootView)
    }

    /**
     * Init custom attributes.
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        init(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.OptionsFabLayout, 0, 0)

        bgColor = a.getColor(
            R.styleable.OptionsFabLayout_background_color,
            ContextCompat.getColor(context, R.color.colorWhiteBackground)
        )

        mRootView.setBackgroundColor(Color.TRANSPARENT)
        mRootView.visibility = View.INVISIBLE

        mFabWithOptions = FabWithOptions(context, attrs)
        addView(mFabWithOptions)

        val params = mFabWithOptions!!.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        mFabWithOptions!!.layoutParams = params

        a.recycle()
    }

    /**
     * Sets Main FloatingActionButton ClickListener.
     *
     * @param listener listener to set.
     */
    fun setMainFabOnClickListener(listener: OnClickListener) {
        mFabWithOptions!!.setMainFabOnClickListener(listener, this)
    }

    /**
     * Closes fab menu.
     */
    fun closeOptionsMenu() {
        mFabWithOptions!!.closeOptionsMenu()
    }

    /**
     * Sets mini fab's colors.
     *
     * @param colors colors to set.
     */
    fun setMiniFabsColors(vararg colors: Int) {
        for (i in mFabWithOptions!!.miniFabs.indices) {
            mFabWithOptions!!.miniFabs[i]
                .fab?.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context!!, colors[i]))
        }
    }

    /**
     * Set a listener that will be notified when a menu fab is selected.
     *
     * @param listener listener to set.
     */
    fun setMiniFabSelectedListener(listener: OnMiniFabSelectedListener) {
        for (miniFabIndex in 0 until mFabWithOptions!!.miniFabs.size) {
            mFabWithOptions!!.miniFabs[miniFabIndex].fab?.setOnClickListener {
                listener.onMiniFabSelected(
                    mFabWithOptions!!.getMenuItem(miniFabIndex)
                )
            }
            mFabWithOptions!!.miniFabs[miniFabIndex].card?.setOnClickListener {
                listener.onMiniFabSelected(
                    mFabWithOptions!!.getMenuItem(miniFabIndex)
                )
            }
            mFabWithOptions!!.miniFabs[miniFabIndex].card?.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN)
                    (v as CardView).setCardBackgroundColor(Color.parseColor("#ebebeb"))
                else if (event.action == MotionEvent.ACTION_UP)
                    (v as CardView).setCardBackgroundColor(
                        ContextCompat.getColor(context!!, R.color.cardview_light_background)
                    )
                true
            }
        }
    }

    /**
     * Listener for handling events on mini fab's.
     */
    interface OnMiniFabSelectedListener {
        fun onMiniFabSelected(fabItem: MenuItem)
    }

}

