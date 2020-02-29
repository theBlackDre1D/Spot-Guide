package com.example.spotguide.core.base

import java.io.Serializable

abstract class BaseInputFragment<P: Serializable>: BaseFragment() {

    val params: P? by lazy { arguments?.getSerializable("params") as P }

}