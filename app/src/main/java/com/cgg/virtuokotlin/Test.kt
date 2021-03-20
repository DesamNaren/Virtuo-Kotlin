package com.cgg.virtuokotlin

    fun one(x: Int, y: Int): Int {
        return x.plus(y)
    }

    fun two(x:Int, y:Int, first:(Int, Int)->Int):Int{
        return x.plus(y).plus(first(1, 2))
    }

    fun main(){
        print(two(10, 20, ::one))
    }
