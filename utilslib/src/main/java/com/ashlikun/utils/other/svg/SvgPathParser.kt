package com.ashlikun.utils.other.svg

import android.graphics.Path
import android.graphics.PointF
import java.text.ParseException

/**
 * 用于解析Android SDK可理解的[Path]项的Svg路径的实体。获得 以避免重写。
 * 请使用官方的PathParser.createPathFromPathData(str)
 */
class SvgPathParser {
    private var mCurrentToken = 0
    private val mCurrentPoint = PointF()
    private var mLength = 0
    private var mIndex = 0
    private var mPathString: String? = null

    @Throws(ParseException::class)
    fun parsePath(s: String?): Path {
        mCurrentPoint[Float.NaN] = Float.NaN
        mPathString = s
        mIndex = 0
        mLength = mPathString!!.length
        val tempPoint1 = PointF()
        val tempPoint2 = PointF()
        val tempPoint3 = PointF()
        val p = Path()
        p.fillType = Path.FillType.WINDING
        var firstMove = true
        while (mIndex < mLength) {
            val command = consumeCommand()
            val relative = mCurrentToken == TOKEN_RELATIVE_COMMAND
            when (command) {
                'M', 'm' -> {
                    // move命令
                    var firstPoint = true
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative && mCurrentPoint.x != Float.NaN)
                        if (firstPoint) {
                            p.moveTo(tempPoint1.x, tempPoint1.y)
                            firstPoint = false
                            if (firstMove) {
                                mCurrentPoint.set(tempPoint1)
                                firstMove = false
                            }
                        } else {
                            p.lineTo(tempPoint1.x, tempPoint1.y)
                        }
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'C', 'c' -> {
                    // 曲线命令
                    if (mCurrentPoint.x == Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative)
                        consumeAndTransformPoint(tempPoint2, relative)
                        consumeAndTransformPoint(tempPoint3, relative)
                        p.cubicTo(
                            tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y, tempPoint3.x,
                            tempPoint3.y
                        )
                    }
                    mCurrentPoint.set(tempPoint3)
                }

                'L', 'l' -> {
                    // 行命令
                    if (mCurrentPoint.x == Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        consumeAndTransformPoint(tempPoint1, relative)
                        p.lineTo(tempPoint1.x, tempPoint1.y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'H', 'h' -> {
                    // 水平行命令
                    if (mCurrentPoint.x == Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        var x = consumeValue()
                        if (relative) {
                            x += mCurrentPoint.x
                        }
                        p.lineTo(x, mCurrentPoint.y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'V', 'v' -> {
                    // 垂直线命令
                    if (mCurrentPoint.x == Float.NaN) {
                        throw ParseException("Relative commands require current point", mIndex)
                    }
                    while (advanceToNextToken() == TOKEN_VALUE) {
                        var y = consumeValue()
                        if (relative) {
                            y += mCurrentPoint.y
                        }
                        p.lineTo(mCurrentPoint.x, y)
                    }
                    mCurrentPoint.set(tempPoint1)
                }

                'Z', 'z' -> {
                    // 关闭命令
                    p.close()
                }
            }
        }
        return p
    }

    private fun advanceToNextToken(): Int {
        while (mIndex < mLength) {
            val c = mPathString!![mIndex]
            if (c in 'a'..'z') {
                return TOKEN_RELATIVE_COMMAND.also { mCurrentToken = it }
            } else if (c in 'A'..'Z') {
                return TOKEN_ABSOLUTE_COMMAND.also { mCurrentToken = it }
            } else if (c in '0'..'9' || c == '.' || c == '-') {
                return TOKEN_VALUE.also { mCurrentToken = it }
            }
            // 跳过无法识别的字符
            ++mIndex
        }
        return TOKEN_EOF.also { mCurrentToken = it }
    }

    @Throws(ParseException::class)
    private fun consumeCommand(): Char {
        advanceToNextToken()
        if (mCurrentToken != TOKEN_RELATIVE_COMMAND && mCurrentToken != TOKEN_ABSOLUTE_COMMAND) {
            throw ParseException("Expected command", mIndex)
        }
        return mPathString!![mIndex++]
    }

    @Throws(ParseException::class)
    private fun consumeAndTransformPoint(out: PointF, relative: Boolean) {
        out.x = consumeValue()
        out.y = consumeValue()
        if (relative) {
            out.x += mCurrentPoint.x
            out.y += mCurrentPoint.y
        }
    }

    @Throws(ParseException::class)
    private fun consumeValue(): Float {
        advanceToNextToken()
        if (mCurrentToken != TOKEN_VALUE) {
            throw ParseException("Expected value", mIndex)
        }
        var start = true
        var seenDot = false
        var index = mIndex
        while (index < mLength) {
            val c = mPathString!![index]
            if (!(c in '0'..'9') && (c != '.' || seenDot) && (c != '-' || !start)) {
                // 价值终结
                break
            }
            if (c == '.') {
                seenDot = true
            }
            start = false
            ++index
        }
        if (index == mIndex) {
            throw ParseException("Expected value", mIndex)
        }
        val str = mPathString!!.substring(mIndex, index)
        return try {
            val value = str.toFloat()
            mIndex = index
            value
        } catch (e: NumberFormatException) {
            throw ParseException("Invalid float value '$str'.", mIndex)
        }
    }

    companion object {
        private const val TOKEN_ABSOLUTE_COMMAND = 1
        private const val TOKEN_RELATIVE_COMMAND = 2
        private const val TOKEN_VALUE = 3
        private const val TOKEN_EOF = 4
    }
}