package com.edwardwongtl.rides.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.edwardwongtl.rides.databinding.ItemVehicleListBinding
import com.edwardwongtl.rides.model.Vehicle

class VehicleListAdapter(
    private val vehicles: List<Vehicle>,
    private val lifecycleOwner: LifecycleOwner
) :
    RecyclerView.Adapter<VehicleListAdapter.VehicleViewHolder>() {

    class VehicleViewHolder(val viewBinding: ItemVehicleListBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val binding =
            ItemVehicleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val currentVehicle = vehicles[position]
        with(holder.viewBinding) {
            vehicle = currentVehicle
            lifecycleOwner = this@VehicleListAdapter.lifecycleOwner
        }
    }

    override fun getItemCount(): Int = vehicles.size
}