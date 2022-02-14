package com.kigya.lab1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.animation.Easing
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kigya.lab1.databinding.FragmentWindowBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

private lateinit var lineChart: LineChart
private var scoreList = ArrayList<Score>()

class WindowFragment : BottomSheetDialogFragment(), OnBottomSheetCallbacks {
    private var _binding: FragmentWindowBinding? = null
    private val binding get() = _binding!!
    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).setOnBottomSheetCallbacks(this)
        _binding = FragmentWindowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.openContextText.setOnClickListener {
            (activity as MainActivity).openBottomSheet()
        }

        binding.filterImage.setOnClickListener {
            if (currentState == BottomSheetBehavior.STATE_EXPANDED) {
                (activity as MainActivity).closeBottomSheet()
            } else {
                (activity as MainActivity).openBottomSheet()
            }
        }

        lineChart = view.findViewById(R.id.lineChart)

        initLineChart()
        setDataToLineChart()
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        currentState = newState
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                binding.openContextText.setText(R.string.open_context)
                binding.filterImage.setImageResource(R.drawable.ic_baseline_filter_list_24)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.openContextText.setText(R.string.execution)
                binding.filterImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            // TODO: when the bottom sheet is moving update data
        }
    }
}

data class Score(
    val name:String,
    val score: Int,
)

private fun initLineChart() {

//        hide grid lines
    lineChart.axisLeft.setDrawGridLines(false)
    val xAxis: XAxis = lineChart.xAxis
    xAxis.setDrawGridLines(false)
    xAxis.setDrawAxisLine(false)

    //remove right y-axis
    lineChart.axisRight.isEnabled = false

    //remove legend
    lineChart.legend.isEnabled = false


    //remove description label
    lineChart.description.isEnabled = false


    //add animation
    lineChart.animateX(1000, Easing.EaseInSine)

    // to draw label on xAxis
    xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
    xAxis.valueFormatter = MyAxisFormatter()
    xAxis.setDrawLabels(true)
    xAxis.granularity = 1f
    xAxis.labelRotationAngle = +90f

}


class MyAxisFormatter : IndexAxisValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return if (index < scoreList.size) {
            scoreList[index].name
        } else {
            ""
        }
    }
}

private fun setDataToLineChart() {
    //now draw bar chart with dynamic data
    val entries: ArrayList<Entry> = ArrayList()

    scoreList = getScoreList()

    //you can replace this data object with  your custom object
    for (i in scoreList.indices) {
        val score = scoreList[i]
        entries.add(Entry(i.toFloat(), score.score.toFloat()))
    }

    val lineDataSet = LineDataSet(entries as List<Entry>?, "")

    val data = LineData(lineDataSet)
    lineChart.data = data

    lineChart.invalidate()
}

// simulate api call
// we are initialising it directly
private fun getScoreList(): ArrayList<Score> {
    scoreList.add(Score("John", 56))
    scoreList.add(Score("Rey", 75))
    scoreList.add(Score("Steve", 85))
    scoreList.add(Score("Kevin", 45))
    scoreList.add(Score("Jeff", 63))

    return scoreList
}


