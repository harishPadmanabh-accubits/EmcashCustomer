package com.emcash.customerapp.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.emcash.customerapp.R
import com.emcash.customerapp.extensions.openActivity
import com.emcash.customerapp.extensions.showShortToast
import com.emcash.customerapp.model.transactions.FilterDurationResponse
import com.emcash.customerapp.ui.history.adapters.DurationAdapter
import com.emcash.customerapp.ui.history.adapters.DurationItemClickListener
import com.emcash.customerapp.ui.history.adapters.TransactionsTabAdapter
import com.emcash.customerapp.ui.home.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.lay_duration_filter.*
import kotlinx.android.synthetic.main.layout_types_filter.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionHistory : FragmentActivity(), DurationItemClickListener {

    private val viewModel: TransactionHistoryViewModel by viewModels()
    private var startDate: String = ""
    private var endDate: String = ""
    private var durationFilterCustom = false

    var listDates = java.util.ArrayList<Date>()
    var dateArray = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        setupTabs()
        iv_back.setOnClickListener {
            onBackPressed()
        }
        iv_filter.setOnClickListener {
            if (fl_holder.isVisible) {
                fl_holder.visibility = View.GONE
            } else {
                fl_holder.visibility = View.VISIBLE
                showTypes()


            }
        }
        configureFilterDurations()

        ll_holder.visibility = View.GONE
        calenderView.visibility = View.GONE

        configureCalendarView()



        iv_back_type.setOnClickListener {
            fl_holder.visibility = View.GONE

        }
        iv_duration_back.setOnClickListener {
            showTypes()
        }

        btn_filter.setOnClickListener {
            if (durationFilterCustom) {
                listDates = calenderView.selectedDates as java.util.ArrayList<Date>
                if (isFutureDateSelected(listDates)) {
                    showShortToast(getString(R.string.error_future_date_selected))
                } else {
                    if (listDates.size <= 1) {
                        showShortToast(getString(R.string.error_select_start_end_date))
                    } else {
                        startDate = listDates[0].toString()
                        endDate = listDates[listDates.size - 1].toString()
                        dateArray.add(0, dateFormatFromCalender("yyyy-MM-dd", startDate))
                        dateArray.add(1, dateFormatFromCalender("yyyy-MM-dd", endDate))

                        viewModel.sendDate(dateArray)
                        fl_holder.visibility = View.GONE
                    }
                }
            } else {
                if (startDate.isEmpty()) {
                    showShortToast("Select a Date")
                } else {
                    dateArray.add(0, startDate)
                    dateArray.add(1, getDayAgo("yyyy-MM-dd", 0).toString())
                    viewModel.sendDate(dateArray)
                    fl_holder.visibility = View.GONE
                }
            }
        }

        rg_type.setOnCheckedChangeListener { _, _ ->

            rb_emcashSent.setOnClickListener {
                viewpager_tabs.currentItem = 2
                fl_holder.visibility = View.GONE

            }
            rb_emcashrecieved.setOnClickListener {
                viewpager_tabs.currentItem = 1
                fl_holder.visibility = View.GONE

            }
            rb_rejected.setOnClickListener {
                viewModel.sendStatus("4")
                fl_holder.visibility = View.GONE

            }
            rb_failed.setOnClickListener {
                viewModel.sendStatus("3")
                fl_holder.visibility = View.GONE

            }
            rb_pending.setOnClickListener {
                viewModel.sendStatus("2")
                fl_holder.visibility = View.GONE

            }
            rb_success.setOnClickListener {
                viewModel.sendStatus("1")
                fl_holder.visibility = View.GONE


            }
            rb_date.setOnClickListener {
                showDurations()
            }

        }


    }

    private fun isFutureDateSelected(listDates: java.util.ArrayList<Date>): Boolean {
        listDates.forEach { date ->
            val currentDate = Calendar.getInstance().time
            Timber.e("currentDate.before(date) $currentDate $date ${currentDate.before(date)}")
            if (currentDate.before(date)) return true
        }
        return false
    }

    private fun configureCalendarView() {

        val timeZone = TimeZone.getDefault()
        val locale = Locale.getDefault()

        val nextYear = Calendar.getInstance(timeZone, locale)
        nextYear.add(Calendar.YEAR, 1)


        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -2)

        calenderView.init(lastYear.time, nextYear.time) //
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(Date())

        calenderView.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) {
                updateUIwithCalendar(calenderView.selectedDates)
            }

            override fun onDateUnselected(date: Date?) {
            }

        })

    }

    private fun updateUIwithCalendar(selectedDates: List<Date>) {

        tv_toDate.text = dateFormatFromCalender("dd-MMM-YYYY", selectedDates.last().toString())
        tv_fromDate.text = dateFormatFromCalender("dd-MMM-YYYY", selectedDates.first().toString())
    }

    private fun showTypes() {
        ll_type.visibility = View.VISIBLE
        ll_duration.visibility = View.GONE
    }

    private fun showDurations() {
        ll_type.visibility = View.GONE
        ll_duration.visibility = View.VISIBLE
    }

    private fun configureFilterDurations() {
        val durations = ArrayList<FilterDurationResponse>()
        durations.add(FilterDurationResponse(1, getString(R.string.two_days)))
        durations.add(FilterDurationResponse(2, getString(R.string.one_week)))
        durations.add(FilterDurationResponse(3, getString(R.string.one_month)))
        durations.add(FilterDurationResponse(4, getString(R.string.custom)))

        rv_duration.apply {
            adapter = DurationAdapter(durations, this@TransactionHistory)
        }
    }


    private fun setupTabs() {
        viewpager_tabs.adapter =
            TransactionsTabAdapter(
                this
            )
        TabLayoutMediator(tab_layout, viewpager_tabs) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.all)
                1 -> tab.text = getString(R.string.inbound)
                2 -> tab.text = getString(R.string.outbound)
            }
        }.attach()

    }

    override fun onBackPressed() {
        openActivity(HomeActivity::class.java)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onDurationClicked(duration: FilterDurationResponse) {
        when (duration.id) {
            1 -> setFilterTwoDaysAgo()
            2 -> setFilterOneWeekAgo()
            3 -> setFilterOneMonthAgo()
            4 -> showCustomDateFilter()
        }

    }

    private fun setFilterTwoDaysAgo() {
        ll_holder.visibility = View.GONE
        calenderView.visibility = View.GONE
        endDate = ""
        startDate = getDayAgo("yyyy-MM-dd", -2).toString()
        durationFilterCustom = false
    }

    private fun setFilterOneWeekAgo() {
        ll_holder.visibility = View.GONE
        calenderView.visibility = View.GONE
        startDate = getDayAgo("yyyy-MM-dd", -7).toString()
        durationFilterCustom = false
    }

    private fun setFilterOneMonthAgo() {
        ll_holder.visibility = View.GONE
        calenderView.visibility = View.GONE
        startDate = getDayAgo("yyyy-MM-dd", -30).toString()
        endDate = ""
        durationFilterCustom = false
    }

    private fun showCustomDateFilter() {
        ll_holder.visibility = View.VISIBLE
        calenderView.visibility = View.VISIBLE
        startDate = ""
        endDate = ""
        durationFilterCustom = true
    }
}


@SuppressLint("SimpleDateFormat")
fun dateFormatFromCalender(dateFormat: String, dateStr: String): String {
    Timber.e("Date in cal $dateFormat $dateStr")
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
    val destFormat = SimpleDateFormat(dateFormat)
    sourceFormat.timeZone = utc
    val convertedDate = sourceFormat.parse(dateStr)
    return destFormat.format(convertedDate)
}

@SuppressLint("SimpleDateFormat")
fun getDayAgo(dateFormat: String?, days: Int): String? {
    val cal = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}