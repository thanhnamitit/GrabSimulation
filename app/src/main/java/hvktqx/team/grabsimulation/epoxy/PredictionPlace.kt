package hvktqx.team.grabsimulation.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import hvktqx.team.grabsimulation.R
import hvktqx.team.grabsimulation.data.model.Spot
import hvktqx.team.grabsimulation.epoxy.base.ViewBaseModel
import hvktqx.team.grabsimulation.extension.bind

@ModelView(
  defaultLayout = R.layout.item_prediction_place,
  baseModelClass = ViewBaseModel::class
)

class PredictionPlace @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private val addressTextView: TextView? by bind(R.id.tv_address)

  @ModelProp
  lateinit var spot: Spot

  @AfterPropsSet
  fun bindData() {
    addressTextView?.text = spot.address
  }
}