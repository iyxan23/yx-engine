package com.iyxan23.yx.html

enum class HtmlToken {
    OPEN_TAG,         // <
    TAG_NAME,         // <{tag name}
    ATTRIBUTE_NAME,   //  {tag name} {attribute name}=
    ATTRIBUTE_VALUE,  //             {attribute name}={attribute_value}
    CLOSE_TAG,        //                                               >
    TEXT_VALUE,       //                                               >{text value}
    NO_CHILD_CLOSE,   //                                               />
    OPEN_CLOSING_TAG, // </
}