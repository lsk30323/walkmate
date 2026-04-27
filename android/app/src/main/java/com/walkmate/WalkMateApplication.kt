package com.walkmate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WalkMateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // DynamicColors intentionally NOT applied — preserve emerald brand identity.
        // 결정 근거: design-system/mapping-rationale.md "왜 DynamicColors 안 씀?"
        // 트리거 시 재검토: 사내 부서별 컬러 적용 요구 발생 시 DynamicColors.applyToActivitiesIfAvailable(this) 호출.
    }
}
