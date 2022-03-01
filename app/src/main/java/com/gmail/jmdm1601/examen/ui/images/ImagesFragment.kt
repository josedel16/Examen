package com.gmail.jmdm1601.examen.ui.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.jmdm1601.examen.R
import com.gmail.jmdm1601.examen.core.Constants
import com.gmail.jmdm1601.examen.core.Resource
import com.gmail.jmdm1601.examen.databinding.FragmentImagesBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private var _binding: FragmentImagesBinding? = null
    private val mBinding get() = _binding!!

    private val mImagesViewModel: ImagesViewModel by viewModels()
    private val mAdapter: ImagesAdapter = ImagesAdapter(mutableListOf())

    private val resultLauncherGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.let {
                    mImagesViewModel.uploadImage(it)
                }
            }
        }

    private val resultLauncherCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.extras?.get(DATA)?.let {
                    mImagesViewModel.uploadBitmap(it as Bitmap)
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGranted = true
            it.forEach { (_, b) -> isGranted = isGranted && b }

            if (isGranted) {
                showMaterialDialog()
            } else {
                showMaterialDialogNoPermissions()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModel()
    }

    private fun setupViews() {
        mBinding.fab.setOnClickListener {
            if (checkPermission()) {
                showMaterialDialog()
            } else {
                requestPermission()
            }
        }

        mBinding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(context, resources.getInteger(R.integer.span_count_grid))
            adapter = mAdapter
        }
    }

    private fun setupViewModel() {
        mImagesViewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.image_added_successfully),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is Resource.Failure -> {
                    mBinding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        mBinding.root,
                        getString(R.string.error_adding_image),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        mImagesViewModel.images.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.isEmpty()) {
                    mBinding.tvWithoutImages.visibility = View.VISIBLE
                } else {
                    mBinding.tvWithoutImages.visibility = View.GONE
                }
                mAdapter.setImages(it)
            } else {
                mBinding.tvWithoutImages.visibility = View.VISIBLE
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
            )
        )
    }

    private fun showMaterialDialogNoPermissions() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_permissions_title))
            .setMessage(getString(R.string.dialog_permissions_content))
            .setNegativeButton(getString(R.string.settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts(Constants.PACKAGE, requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }

    private fun showMaterialDialog() {
        val options = resources.getStringArray(R.array.options_dialog_add_image)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_picker_title))
            .setItems(options) { _, which ->
                when (which) {
                    Constants.OPTION_CAMERA -> takePictureIntent()
                    Constants.OPTION_GALLERY -> openGalleryIntent()
                }
            }
            .show()
    }

    private fun takePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncherCamera.launch(intent)
    }

    private fun openGalleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncherGallery.launch(intent)
    }

    companion object {
        const val DATA = "data"
    }
}