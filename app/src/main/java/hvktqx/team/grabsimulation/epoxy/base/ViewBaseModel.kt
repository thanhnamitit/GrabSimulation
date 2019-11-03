package hvktqx.team.grabsimulation.epoxy.base

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel

/**
 * Default height is set for view in horizontal recycler view
 * Remember to set these property when building your model if needed
 */

fun Context.dip(size: Int): Int = (resources.displayMetrics.density * size + 0.5f).toInt()

data class Spacing(
  val start: Int = 0,
  val top: Int = 0,
  val end: Int = 0,
  val bottom: Int = 0
) {
  companion object {
    val NONE = Spacing()
  }
}

abstract class ViewBaseModel<T : View> : EpoxyModel<T>() {

  @EpoxyAttribute
  @DrawableRes
  open var backgroundRes: Int = 0

  @EpoxyAttribute
  @DrawableRes
  open var foregroundRes: Int = 0

  @EpoxyAttribute
  open var width: Int? = null

  @EpoxyAttribute
  open var height: Int = LayoutParams.WRAP_CONTENT

  @EpoxyAttribute
  open var padding: Spacing = Spacing.NONE

  @EpoxyAttribute
  open var margin: Spacing = Spacing.NONE

  @EpoxyAttribute(DoNotHash)
  open var onClickListener: OnClickListener? = null

  override fun unbind(view: T) {
    super.unbind(view)
    view.setOnClickListener(null)
  }

  override fun bind(view: T) {
    super.bind(view)
    view.setBackgroundResource(backgroundRes)
    setForegroundRes(view)
    setPadding(view)
    setMargin(view)
    setWidth(view)
    setHeight(view)
    view.setOnClickListener(onClickListener)
  }

  @Suppress("UNCHECKED_CAST")
  override fun bind(view: T, previouslyBoundModel: EpoxyModel<*>) {
    super.bind(view, previouslyBoundModel)
    val that: ViewBaseModel<T> = previouslyBoundModel as ViewBaseModel<T>
    if (backgroundRes != that.backgroundRes) {
      view.setBackgroundResource(backgroundRes)
    }
    if (foregroundRes != that.foregroundRes) {
      setForegroundRes(view)
    }
    if (padding != that.padding) {
      setPadding(view)
    }
    if (margin != that.margin) {
      setMargin(view)
    }
    if (onClickListener != that.onClickListener) {
      view.setOnClickListener(onClickListener)
    }
    if (width != that.width) {
      setWidth(view)
    }
    if (height != that.height) {
      setHeight(view)
    }
  }

  private fun setPadding(view: T) {
    val context = view.context
    view.setPadding(
      context.dip(padding.start),
      context.dip(padding.top),
      context.dip(padding.end),
      context.dip(padding.bottom)
    )
  }

  private fun setMargin(view: T) {
    view.updateLayoutParams<MarginLayoutParams> {
      val context = view.context
      setMargins(
        context.dip(margin.start),
        context.dip(margin.top),
        context.dip(margin.end),
        context.dip(margin.bottom)
      )
    }
  }

  private fun setForegroundRes(view: T) {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      view.foreground = if (foregroundRes == 0) {
        null
      } else {
        ContextCompat.getDrawable(view.context, foregroundRes)
      }
    }
  }

  private fun setWidth(view: T) {
    val w = this@ViewBaseModel.width ?: return
    view.updateLayoutParams {
      width = w
    }
  }

  private fun setHeight(view: T) {
    val h = this@ViewBaseModel.height
    view.updateLayoutParams {
      height = h
    }
  }
}
