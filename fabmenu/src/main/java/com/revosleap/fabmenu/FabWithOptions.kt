package com.revosleap.fabmenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

@SuppressLint("RestrictedApi")
class FabWithOptions @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val mFabWithTitles = ArrayList<FabWithTitleView>()
    private var mMainFab: FloatingActionButton? = null
    private val mFabMenu = MenuBuilder(getContext())
    /**
     * @return returns true if menu opened,false otherwise.
     */
    internal var isMiniFabsOpened = false
        private set
    private var mOptionsFabLayout: OptionsFabLayout? = null

    /**
     * @return returns all mini fab's with title.
     */
    val miniFabs: List<FabWithTitleView>
        get() = mFabWithTitles

    init {
        init(context, attrs)
    }

    private fun init(context: Context) {
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val rootView = View.inflate(context, R.layout.fab_options, this)
        mMainFab = rootView.findViewById<View>(R.id.mega_fab) as FloatingActionButton
        orientation = VERTICAL
        clipChildren = false
    }

    /**
     * Init custom attributes.
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        init(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.OptionsFabLayout, 0, 0)
        setupMainFab(context, a)
        setupMiniFabs(context, a)
        a.recycle()
    }

    /**
     * Set Main FloatingActionButton parameters.
     */
    private fun setupMainFab(context: Context, a: TypedArray) {
        val mainFabDrawable = a.getResourceId(R.styleable.OptionsFabLayout_src, -1)

        val color = a.getColor(
            R.styleable.OptionsFabLayout_color,
            ContextCompat.getColor(context, R.color.colorAccent)
        )

        mMainFab!!.backgroundTintList = ColorStateList.valueOf(color)

        if (mainFabDrawable != -1) {
            mMainFab!!.setImageResource(mainFabDrawable)
        }
        //Set margins on Main FloatingActionButton.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val params = mMainFab!!.layoutParams as MarginLayoutParams
            params.setMargins(0, 0, pxToDp(context, 4), 0)
            if (Build.VERSION.SDK_INT >= 17)
                params.marginEnd = pxToDp(context, 4)
            mMainFab!!.layoutParams = params
        }
    }

    /**
     * Set mini fab's titles,drawables,colors.
     */
    private fun setupMiniFabs(context: Context, a: TypedArray) {
        val inflater = MenuInflater(context)

        val miniFabsColor = a.getColor(
            R.styleable.OptionsFabLayout_options_color,
            ContextCompat.getColor(context, R.color.colorPrimary)
        )

        val menuId = a.getResourceId(R.styleable.OptionsFabLayout_options_menu, -1)
        if (menuId != -1) {
            inflater.inflate(menuId, mFabMenu)
            for (i in 0 until mFabMenu.size()) {
                val mFabWithTitleView = FabWithTitleView(context)

                setupMarginsForMiniFab(context, mFabWithTitleView)

                mFabWithTitleView.setMiniFabTitle(mFabMenu.getItem(i).title)
                mFabWithTitleView.setFabIcon(mFabMenu.getItem(i).icon)
                mFabWithTitleView.setMiniFabColor(miniFabsColor)

                addView(mFabWithTitleView, 0)
                mFabWithTitles.add(mFabWithTitleView)
            }
            setMiniFabsEnable(false)
        }
    }

    /**
     * Set margins for mini fab's.
     */
    private fun setupMarginsForMiniFab(context: Context, FabWithTitleView: FabWithTitleView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val miniFabParams = FabWithTitleView.fab!!.layoutParams as MarginLayoutParams
            miniFabParams.setMargins(0, 0, pxToDp(context, 12), 0)
            if (Build.VERSION.SDK_INT >= 17)
                miniFabParams.marginEnd = pxToDp(context, 12)
            FabWithTitleView.fab!!.layoutParams = miniFabParams
        }
    }

    /**
     * Set mini fab's state (enabled or disabled).
     */

    fun setMiniFabsEnable(enable: Boolean) {
        for (mFabWithlabel in mFabWithTitles) {
            mFabWithlabel.card!!.visibility = if (enable) View.VISIBLE else View.GONE
            mFabWithlabel.fab!!.visibility = if (enable) View.VISIBLE else View.GONE
        }
    }

    /**
     * Set Main FloatingActionButton ClickListener.
     *
     * @param listener listener to set.
     * @param layout   layout that contains FloatingActionButton.
     */
    fun setMainFabOnClickListener(listener: OnClickListener, layout: OptionsFabLayout) {

        mOptionsFabLayout = layout
        mOptionsFabLayout!!.mRootView.setOnClickListener { closeOptionsMenu() }

        mMainFab!!.setOnClickListener { view ->
            //Show menu when ripple ends.
            Handler().postDelayed({
                if (!isMiniFabsOpened) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        layout.elevation = pxToDp(context, 5).toFloat()
                    } else {
                        layout.bringToFront()
                    }
                    visibilitySetup(View.VISIBLE)
                    isMiniFabsOpened = true
                } else {
                    layout.closeOptionsMenu()
                    isMiniFabsOpened= false
                }
            }, 50)
        }
    }

    internal fun getMenuItem(index: Int): MenuItem {
        return mFabMenu.getItem(index)
    }

    /**
     * Closes options menu.
     */
    internal fun closeOptionsMenu() {
        isMiniFabsOpened = false
        visibilitySetup(View.GONE)
    }

    /**
     * Closing animation.
     *
     * @param myView view that starts that animation.
     */
    private fun shrinkAnim(myView: View) {
        val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        anim.duration = 130
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                if (myView is FabWithTitleView) {
                    myView.card!!.visibility = View.GONE
                    myView.fab!!.visibility = View.GONE
                }
                myView.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        myView.startAnimation(anim)
    }

    /**
     * Menu opening animation.
     *
     * @param myView view that starts that animation.
     */
    private fun enlargeAnim(myView: View) {
        myView.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(context, R.anim.enlarge)
        myView.startAnimation(anim)
    }

    /**
     * Set menus visibility (visible or invisible).
     */
    private fun visibilitySetup(visible: Int) {

        if (visible == View.VISIBLE) {
            val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            anim.duration = 50
            mOptionsFabLayout!!.mRootView.visibility = visible
            mOptionsFabLayout!!.mRootView.startAnimation(anim)
        }
        if (visible == View.VISIBLE)
            for (i in mFabWithTitles.indices) {
                mFabWithTitles[i].visibility = View.VISIBLE

                Handler().postDelayed({ enlargeAnim(mFabWithTitles[i].fab!!) }, (i * 15).toLong())
                if (mFabWithTitles[i].titleEnable)
                    Handler().postDelayed({
                        mFabWithTitles[i].card!!.visibility = View.VISIBLE
                        mFabWithTitles[i]
                            .card!!
                            .startAnimation(
                                AnimationUtils.loadAnimation(context, R.anim.fade_and_translate)
                            )
                    }, (mFabWithTitles.size * 18).toLong())
            }
        else {
            shrinkAnim(mOptionsFabLayout!!.mRootView)
            for (i in mFabWithTitles.indices) {
                shrinkAnim(mFabWithTitles[i])
            }

        }
    }

    companion object {

        /**
         * Converts px to dp.
         */
        fun pxToDp(context: Context, pixel: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                pixel.toFloat(),
                context.resources.displayMetrics
            ).toInt()
        }
    }
}
