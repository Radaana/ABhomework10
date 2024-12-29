package otus.gpb.homework.viewandresources
import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.withStyledAttributes
import kotlinx.parcelize.Parcelize
class CartItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cart_item_style
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    private var title: String = ""
        set(value) {
            field = value
            findViewById<TextView>(R.id.itemTitle).text = value
        }

    private var category: String = ""
        set(value) {
            field = value
            findViewById<TextView>(R.id.itemCategory).text = value
        }

    private var price: String = ""
        set(value) {
            field = value
            findViewById<TextView>(R.id.itemPrice).text = value
        }

    init {
        isSaveEnabled = true
        inflate(getContext(), R.layout.cart_item, this)
        initPanel(attrs, defStyleAttr)
        initItemData(attrs, defStyleAttr)
    }

    @Parcelize
    private data class CartItemState(val superState: Parcelable?, val title: String, val category: String, val price: String): Parcelable

    override fun onSaveInstanceState(): Parcelable {
        return CartItemState(super.onSaveInstanceState(), title, category, price)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is CartItemState) {
            super.onRestoreInstanceState(state.superState)
            title = state.title
            category = state.category
            price = state.price
        }
    }

    private fun initPanel(attrs: AttributeSet?, defStyleAttr: Int) {
        // for android limitation, before finding attrs, you need to sort all attrs you want to find in ascending order, otherwise some attrs cannot be found
        // https://developer.android.com/reference/android/content/res/Resources.Theme.html#obtainStyledAttributes(android.util.AttributeSet, int[], int, int)
        val toRetrieve = intArrayOf(
            android.R.attr.layout_width,
            android.R.attr.layout_height,
            android.R.attr.orientation,
            android.R.attr.gravity,
            android.R.attr.paddingTop,
            android.R.attr.paddingBottom,
            android.R.attr.background
        ).apply { sort() }

        context.withStyledAttributes(attrs, toRetrieve, defStyleAttr, R.style.cart_item) {
            layoutParams = LayoutParams(
                getInt(toRetrieve.indexOf(android.R.attr.layout_width), LayoutParams.WRAP_CONTENT),
                getInt(toRetrieve.indexOf(android.R.attr.layout_height), LayoutParams.WRAP_CONTENT),
            )
            orientation = getInt(toRetrieve.indexOf(android.R.attr.orientation), HORIZONTAL)
            gravity = getInt(toRetrieve.indexOf(android.R.attr.gravity), CENTER)
            setPadding(
                0,
                getDimensionPixelSize(toRetrieve.indexOf(android.R.attr.paddingTop), 0),
                0,
                getDimensionPixelSize(toRetrieve.indexOf(android.R.attr.paddingBottom), 0),
            )
            background = getDrawable(toRetrieve.indexOf(android.R.attr.background))
        }
    }

    private fun initItemData(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.CartItem, defStyleAttr, R.style.cart_item) {
            title = getString(R.styleable.CartItem_item_title) ?: ""
            category = getString(R.styleable.CartItem_item_category) ?: ""
            price = getString(R.styleable.CartItem_item_price) ?: ""
        }
    }
}