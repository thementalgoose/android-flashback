package tmg.f1stats.home.datepicker

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.bottom_sheet_date_picker.*
import org.koin.android.viewmodel.ext.android.viewModel
import tmg.f1stats.R
import tmg.f1stats.utils.TextAdapter
import tmg.utilities.extensions.subscribeNoError
import tmg.utilities.lifecycle.rx.RxBottomSheetFragment

class DatePickerBottomSheetDialogFragment : RxBottomSheetFragment() {

    private lateinit var adapter: TextAdapter

    private val viewModel: DatePickerViewModel by viewModel()

    override fun layoutId(): Int = R.layout.bottom_sheet_date_picker

    override fun arguments(bundle: Bundle) {

        viewModel.inputs.initialYear(bundle.getInt(keyYear))
    }

    override fun initViews() {

        adapter = TextAdapter()
        adapter.itemClicked = { year ->
            year.toIntOrNull()?.let { viewModel.inputs.selectYear(it) }
        }
        rvTexts.layoutManager = LinearLayoutManager(context)
        rvTexts.adapter = adapter
    }

    override fun observeViewModel() {

        viewModel.outputs
            .showCalendarWithOptions()
            .subscribeNoError {
                adapter.list = it
            }
            .autoDispose()

        viewModel.outputs
            .selectionMade()
            .subscribeNoError {
                Toast.makeText(context, "Selection too $it", Toast.LENGTH_LONG).show()
                this.dismiss()
            }
            .autoDispose()
    }

    companion object {

        private const val keyYear: String = "YEAR"

        fun instance(year: Int): DatePickerBottomSheetDialogFragment {
            val instance = DatePickerBottomSheetDialogFragment()
            instance.arguments = Bundle().apply {
                putInt(keyYear, year)
            }
            return instance
        }
    }
}