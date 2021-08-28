package com.emcash.customerapp.ui.history

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionHistory : FragmentActivity() , DurationItemClickListener {

    private val viewModel: TransactionHistoryViewModel by viewModels()
    private var startDate: String = ""
    private var endDate: String = ""
    private var durationFilterCustom = 0

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
            //setup bottom sheet
            if (fl_holder.isVisible) {
                fl_holder.visibility = View.GONE
            } else {
                fl_holder.visibility = View.VISIBLE
                showTypes()


            }
        }


        durationData()


        ll_holder.visibility = View.GONE
        calenderView.visibility = View.GONE
//        ll_calenderController.visibility=View.GONE

        val timeZone = TimeZone.getDefault();
        val locale = Locale.getDefault();

        val nextYear = Calendar.getInstance(timeZone, locale)
        nextYear.add(Calendar.YEAR, 1)


        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -2)

        calenderView.init(lastYear.time, nextYear.time) //
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(Date())






        iv_back_type.setOnClickListener {
            fl_holder.visibility = View.GONE

        }
        iv_duration_back.setOnClickListener {
            showTypes()
        }

//        iv_backward.setOnClickListener {
//
//
//        }
//
//        iv_forward.setOnClickListener {
//            var nextMonth = Calendar.getInstance(timeZone, locale)
//            nextMonth.add(Calendar.MONTH, 1)
//
//            calenderView.scrollToDate(nextMonth.time)
//
//        }
        btn_filter.setOnClickListener {

            if (durationFilterCustom == 0) {
                if (startDate.isEmpty()) {
                    showShortToast("Select a Date")
                } else {

                    dateArray.add(0, startDate)
                    dateArray.add(1,  getDaysAgo(0).toString())

                    viewModel.sendDate(dateArray)
                    fl_holder.visibility = View.GONE


                }
            } else if (durationFilterCustom == 1) {
                listDates = calenderView.selectedDates as java.util.ArrayList<Date>

                if (listDates.size <= 1) {
                   showShortToast("Select a Dates")
                } else {

                    startDate = listDates[0].toString()
                    endDate = listDates[listDates.size - 1].toString()
                    dateArray.add(0, startDate)
                    dateArray.add(1, endDate)

                    viewModel.sendDate(dateArray)
                    fl_holder.visibility = View.GONE
                    tv_toDate.text = dateFormatFromCalender(endDate)
                    tv_fromDate.text = dateFormatFromCalender(startDate)


                }

            }


        }

        rg_type.setOnCheckedChangeListener { group, checkedId ->

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

    private fun showTypes() {
        ll_type.visibility = View.VISIBLE
        ll_duration.visibility = View.GONE
    }

    private fun showDurations() {
        ll_type.visibility = View.GONE
        ll_duration.visibility = View.VISIBLE
    }

    private fun durationData() {
        val durations = ArrayList<FilterDurationResponse>()
        durations.add(FilterDurationResponse(1, "2 Days"))
        durations.add(FilterDurationResponse(2, "1 Week"))
        durations.add(FilterDurationResponse(3, "1 Month"))
        durations.add(FilterDurationResponse(4, "Custom"))

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
                0 -> tab.apply {
                    text = getString(R.string.all)


                }
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

        if (duration.id == 4) {
            ll_holder.visibility = View.VISIBLE
            calenderView.visibility = View.VISIBLE
            startDate = ""
            endDate=""

            durationFilterCustom = 1

        } else if (duration.id == 3) {
            ll_holder.visibility = View.GONE
            calenderView.visibility = View.GONE
            startDate = getDaysAgo(30).toString()
            endDate=""

            durationFilterCustom = 0

        } else if (duration.id == 2) {
            ll_holder.visibility = View.GONE
            calenderView.visibility = View.GONE
            startDate = getDaysAgo(7).toString()
            durationFilterCustom = 0
        } else if (duration.id == 1) {
            ll_holder.visibility = View.GONE
            calenderView.visibility = View.GONE
            endDate=""
            startDate = getDaysAgo(2).toString()
            durationFilterCustom = 0
        }

    }
}

fun getDaysAgo(daysAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return calendar.time
}
fun dateFormatFromCalender(dateStr: String): String {
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
    val destFormat = SimpleDateFormat("dd-MMM-YYYY")
    sourceFormat.timeZone = utc
    val convertedDate = sourceFormat.parse(dateStr)
    return destFormat.format(convertedDate)
}