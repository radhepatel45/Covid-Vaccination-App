package com.example.vaccine.slotbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidappdemo1.slotbook.CenterRvModal
import com.example.vaccine.R

class CenterRVAdapter(private val centerList: List<CenterRvModal>) :
    RecyclerView.Adapter<CenterRVAdapter.CenterRVViewHolder>() {

    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val centerNameTV: TextView = itemView.findViewById(R.id.idTVCenterName)
        val centerAddressTV: TextView = itemView.findViewById(R.id.idTVCenterAddress)
        val centerTimings: TextView = itemView.findViewById(R.id.idTVCenterTimings)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.idTVVaccineName)
        val centerAgeLimitTV: TextView = itemView.findViewById(R.id.idTVAgeLimit)
        val centerFeeTypeTV: TextView = itemView.findViewById(R.id.idTVFeeType)
        val availabilityTV: TextView = itemView.findViewById(R.id.idTVAvailability)

        fun bind(center: CenterRvModal, context: Context) {
            centerNameTV.text = center.centerName
            centerAddressTV.text = center.centerAddress
            centerTimings.text = "From : ${center.centerFromTime} To : ${center.centerToTime}"
            vaccineNameTV.text = center.vaccineName
            centerAgeLimitTV.text = "Age Limit : ${center.ageLimit}"
            centerFeeTypeTV.text = center.fee_type
            availabilityTV.text = "Availability : ${center.availableCapacity}"

            itemView.setOnClickListener {
                val sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
                val userEmail = sharedPreferences.getString("USER_EMAIL", null)
                val intent = VaccineDetailActivity.newIntent(context, center).apply {
                    putExtra("message_Email", userEmail)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.center_rv_item,
            parent, false
        )
        return CenterRVViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val currentItem = centerList[position]
        holder.bind(currentItem, holder.itemView.context)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val sharedPreferences = context.getSharedPreferences("USER_PREF", Context.MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("USER_EMAIL", null)

            val intent = VaccineDetailActivity.newIntent(context, currentItem).apply {
                putExtra("message_Email", userEmail)
            }
            context.startActivity(intent)
        }

        holder.centerNameTV.text = currentItem.centerName
        holder.centerAddressTV.text = currentItem.centerAddress
        holder.centerTimings.text = ("From : " + currentItem.centerFromTime + " To : " + currentItem.centerToTime)
        holder.vaccineNameTV.text = currentItem.vaccineName
        holder.centerAgeLimitTV.text = "Age Limit : " + currentItem.ageLimit.toString()
        holder.centerFeeTypeTV.text = currentItem.fee_type
        holder.availabilityTV.text = "Availability : " + currentItem.availableCapacity.toString()
    }
}
