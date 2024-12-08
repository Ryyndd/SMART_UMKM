package dev.kelompokceria.smart_umkm.ui.user

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dev.kelompokceria.smart_umkm.R
import dev.kelompokceria.smart_umkm.data.helper.Constant
import dev.kelompokceria.smart_umkm.data.helper.PreferenceHelper
import dev.kelompokceria.smart_umkm.databinding.FragmentProfileBinding
import dev.kelompokceria.smart_umkm.ui.LoginActivity
import dev.kelompokceria.smart_umkm.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    private lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
         sharedPref = PreferenceHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Ambil username dari arguments]
        val username = sharedPref.getString(Constant.PREF_USER_NAME)
        if (username == null) {
            Toast.makeText(requireContext(), "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
        } else {

            userViewModel.getUserByUsername(username)

            // Observe LiveData dari userViewModel
            userViewModel.loggedIn.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    user.image.let {
                            Glide.with(binding.ivProfile.context)
                                .load(it)
                                .placeholder(R.drawable.picture) // Placeholder if image is unavailable
                                .into(binding.ivProfile)
                        } ?: run {
                            binding.ivProfile.setImageResource(R.drawable.picture) // Default image
                        }
                    binding.tvName.text = user.username
                } else {
                    Toast.makeText(requireContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up tombol klik
//        binding.btnShowProfile.setOnClickListener {
//            if (binding.groupProfile.visibility == View.GONE) {
//                binding.groupProfile.visibility = View.VISIBLE  // Tampilkan
//            } else {
//                binding.groupProfile.visibility = View.GONE  // Sembunyikan
//            }
//        }

        binding.btnLogout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("Apakah Anda yakin ingin logout")
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    sharedPref.clear()
                    Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("Logout")
            alert.show()

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
