package dev.kelompokceria.smart_umkm.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.kelompokceria.smart_umkm.R
import dev.kelompokceria.smart_umkm.databinding.FragmentAdminProfileBinding
import dev.kelompokceria.smart_umkm.ui.LoginActivity
import dev.kelompokceria.smart_umkm.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class AdminProfileFragment : Fragment() {

    private lateinit var binding: FragmentAdminProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminProfileBinding.inflate(inflater,container,false)
            // Ambil username dari arguments
        val username = arguments?.getString("KEY_USERNAME")

        if (username == null) {
            Toast.makeText(requireContext(), "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
        } else {
            // Ambil data user berdasarkan username
            userViewModel.getUserByUsername(username)

            // Observe LiveData dari userViewModel
            userViewModel.user.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    // Update UI dengan data user
                    binding.tvNameValue.text = user.name
                    binding.tvEmail.text = user.email
                    binding.tvPhoneValue.text = user.phone
                    binding.tvName.text = user.username
                    binding.tvEmailValue.text = user.email
                } else {
                    Toast.makeText(requireContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnShowProfile.setOnClickListener {
             if (binding.groupProfile.visibility == View.GONE) {
                    binding.groupProfile.visibility = View.VISIBLE  // Tampilkan
                } else {
                    binding.groupProfile.visibility = View.GONE  // Sembunyikan
                }
        }

        binding.btnLogout.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }



}