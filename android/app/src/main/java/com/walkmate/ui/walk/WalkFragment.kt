package com.walkmate.ui.walk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.walkmate.databinding.FragmentWalkBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 산책 화면 — GPS 경로 + 시작/일시정지/종료 + Reverse Geocoding 라벨.
 *
 * Week 0 placeholder. Week 4-5 실제 구현:
 * - LocationService (Foreground Service, foregroundServiceType="location")
 * - Google Maps Fragment + Polyline
 * - 산책 종료 시 Naver Reverse Geocoding 백엔드 프록시 호출 (ADR-006)
 */
@AndroidEntryPoint
class WalkFragment : Fragment() {

    private var _binding: FragmentWalkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
