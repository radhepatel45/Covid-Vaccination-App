package com.example.covidappdemo1.BookingDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccine.R

class BookingDetailsAdapter(private val bookingDetails: ArrayList<BookingDetails>) : RecyclerView.Adapter<BookingDetailsAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalTextView: TextView = itemView.findViewById(R.id.text_view_Hospital_Name)
        val locationTextView: TextView = itemView.findViewById(R.id.idTVCenterAddress)
        val paymentTypeTextView: TextView = itemView.findViewById(R.id.idTVFeeType)
        val vaccineNameTextView: TextView = itemView.findViewById(R.id.idTVVaccineName)
        val timingsTextView : TextView = itemView.findViewById(R.id.idTVCenterTimings)

        fun bind(bookingDetail: BookingDetails) {
            hospitalTextView.text = bookingDetail.HospitalName
            locationTextView.text = bookingDetail.Location
            paymentTypeTextView.text = bookingDetail.PaymentType
            vaccineNameTextView.text = bookingDetail.VaccineName
            timingsTextView.text = bookingDetail.Timings
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.person_appointment_item, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingDetails.size
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(bookingDetails[position])
    }
}
