package com.walkmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.walkmate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity 구조 (명세서 §2 Navigation Component).
 * BottomNavigation 3탭: 홈 / 산책 / 랭킹.
 *
 * 향후 Week 1 진행 시:
 * - 다크모드 전환 버튼 추가 (debug 빌드 한정)
 * - ColorTokenPreviewActivity 진입점 (debug 메뉴)
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNav.setupWithNavController(navHost.navController)
    }
}
