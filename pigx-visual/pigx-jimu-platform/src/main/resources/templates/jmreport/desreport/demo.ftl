<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width">
    <title>积木报表设计器</title>

    <!-- Import via CDN -->
    <link rel="stylesheet" href="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.css">
    <script src="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.js"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/corelib/locale/zh-cn.js"></script>

<body onload="load()">
<div id="jm-sheet-wrapper"></div>
<script>
    function load() {
        const options = {
                showToolbar: false,     //头部操作按钮
                showGrid: false,        //excel表格
                showContextmenu: false, //右键操作按钮
                rpBar: {
                    show: true,
                    style: {
                        "padding-left":"230px",
                    }
                },
                view: {
                    height: () => document.documentElement.clientHeight,
                    width: () => document.documentElement.clientWidth,
            },
            row: {
            len: 100,
                height: 25,
        },
        col: {
            len: 26,
                width: 100,
                minWidth: 60,
                height: 0,
        },
        style: {
            bgcolor: '#ffffff',
                align: 'left',
                valign: 'middle',
                textwrap: false,
                strike: false,
                underline: false,
                color: '#0a0a0a',
                font: {
                name: 'Helvetica',
                    size: 10,
                    bold: false,
                    italic: false,
            },
        },
    };
        //x.spreadsheet.locale('zh-cn');
        var xs = x.spreadsheet('#jm-sheet-wrapper', options).loadData(
            {
                "name": "sheet1",
                "freeze": "A1",
                "styles": [
                    {
                        "bgcolor": "#f4f5f8",
                        "textwrap": true,
                        "color": "#900b09",
                        "border": {
                            "top": [
                                "thin",
                                "#0366d6"
                            ],
                            "bottom": [
                                "thin",
                                "#0366d6"
                            ],
                            "right": [
                                "thin",
                                "#0366d6"
                            ],
                            "left": [
                                "thin",
                                "#0366d6"
                            ]
                        }
                    },
                    {
                        "format": "rmb"
                    },
                    {
                        "textwrap": true
                    },
                    {
                        "bgcolor": "#c45a10"
                    },
                    {
                        "valign": "middle"
                    },
                    {
                        "valign": "top"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#71ae47"
                    },
                    {
                        "bgcolor": "#71ae47"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#a7d08c"
                    },
                    {
                        "bgcolor": "#a7d08c"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#a7d08c",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        }
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        }
                    },
                    {
                        "bgcolor": "#ffffff"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "align": "center"
                    },
                    {
                        "align": "center"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "align": "center",
                        "font": {
                            "bold": true
                        }
                    },
                    {
                        "align": "center",
                        "font": {
                            "bold": true
                        }
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 26
                        }
                    },
                    {
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 26
                        }
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 22
                        }
                    },
                    {
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 22
                        }
                    },
                    {
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        }
                    },
                    {
                        "textwrap": true,
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        }
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 22
                        }
                    },
                    {
                        "align": "right"
                    },
                    {
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "bgcolor": "#5b9cd6"
                    },
                    {
                        "border": {
                            "bottom": [
                                "thin",
                                "#000"
                            ],
                            "top": [
                                "thin",
                                "#000"
                            ],
                            "left": [
                                "thin",
                                "#000"
                            ],
                            "right": [
                                "thin",
                                "#000"
                            ]
                        },
                        "bgcolor": "#ffc001"
                    },
                    {
                        "valign": "middle",
                        "bgcolor": "#ffffff",
                        "align": "center",
                        "font": {
                            "bold": true,
                            "size": 18
                        }
                    },
                    {
                        "font": {
                            "size": 18
                        }
                    },
                    {
                        "font": {
                            "size": 22
                        }
                    }
                ],
                "merges": [
                    "JAAAAAABJ6:JAAAAAABJ8",
                    "C2:H2"
                ],
                "rows": {
                    "1": {
                        "cells": {
                            "2": {
                                "text": "年度各月份佣金收入",
                                "style": 27,
                                "merge": [
                                    0,
                                    5
                                ]
                            },
                            "3": {
                                "style": 28
                            },
                            "4": {
                                "style": 28
                            },
                            "5": {
                                "style": 28
                            },
                            "6": {
                                "style": 28
                            },
                            "7": {
                                "style": 28
                            },
                            "8": {
                                "style": 12
                            },
                            "9": {
                                "style": 12
                            }
                        },
                        "height": 45
                    },
                    "3": {
                        "cells": {
                            "2": {
                                "text": "查询年度：",
                                "style": 24
                            },
                            "3": {
                                "text": "2019"
                            },
                            "5": {
                                "text": "查询机构：",
                                "style": 24
                            },
                            "6": {
                                "text": "总公司"
                            },
                            "7": {
                                "text": "单位：元",
                                "style": 24
                            }
                        }
                    },
                    "5": {
                        "cells": {
                            "2": {
                                "text": "月份",
                                "style": 26
                            },
                            "3": {
                                "text": "佣金/主营业收入",
                                "style": 26
                            },
                            "4": {
                                "text": "累计",
                                "style": 26
                            },
                            "5": {
                                "text": "历史最低水平",
                                "style": 26
                            },
                            "6": {
                                "text": "历史平均水平",
                                "style": 26
                            },
                            "7": {
                                "text": "历史最高水平",
                                "style": 26
                            }
                        }
                    },
                    "6": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "1"
                            },
                            "3": {
                                "style": 22,
                                "text": "\t483834.66"
                            },
                            "4": {
                                "style": 21,
                                "text": "483834.66"
                            },
                            "5": {
                                "style": 21,
                                "text": "\t57569.771"
                            },
                            "6": {
                                "style": 21,
                                "text": "\t216797.62"
                            },
                            "7": {
                                "style": 21,
                                "text": "\t483834.66"
                            }
                        }
                    },
                    "7": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "2"
                            },
                            "3": {
                                "style": 22,
                                "text": "11666578.65"
                            },
                            "4": {
                                "style": 21,
                                "text": "12150413.31"
                            },
                            "5": {
                                "style": 21,
                                "text": "\t22140.00"
                            },
                            "6": {
                                "style": 21,
                                "text": "4985361.57"
                            },
                            "7": {
                                "style": 21,
                                "text": "\t11666578.65"
                            }
                        }
                    },
                    "8": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "3"
                            },
                            "3": {
                                "style": 21,
                                "text": "27080982.08"
                            },
                            "4": {
                                "style": 21,
                                "text": "\t17428381.401"
                            },
                            "5": {
                                "style": 21,
                                "text": "\t73106.2911"
                            },
                            "6": {
                                "style": 21,
                                "text": "16192642.30\t"
                            },
                            "7": {
                                "style": 21,
                                "text": "27080982.08"
                            }
                        }
                    },
                    "9": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "4"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.001"
                            },
                            "4": {
                                "style": 21,
                                "text": "39231395.3911"
                            },
                            "5": {
                                "style": 21,
                                "text": "73106.2911"
                            },
                            "6": {
                                "style": 21,
                                "text": "8513415.34"
                            },
                            "7": {
                                "style": 21,
                                "text": "\t17428381.40"
                            }
                        }
                    },
                    "10": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "5"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.00"
                            },
                            "4": {
                                "style": 21,
                                "text": ""
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "11": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "6"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.00"
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "12": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "7"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.00"
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "13": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "8"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.00"
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "14": {
                        "cells": {
                            "2": {
                                "style": 21,
                                "text": "9"
                            },
                            "3": {
                                "style": 21,
                                "text": "0.00"
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "15": {
                        "cells": {
                            "2": {
                                "style": 21
                            },
                            "3": {
                                "style": 21
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "16": {
                        "cells": {
                            "2": {
                                "style": 21
                            },
                            "3": {
                                "style": 21
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "17": {
                        "cells": {
                            "2": {
                                "style": 21
                            },
                            "3": {
                                "style": 21
                            },
                            "4": {
                                "style": 21
                            },
                            "5": {
                                "style": 21
                            },
                            "6": {
                                "style": 21
                            },
                            "7": {
                                "style": 21
                            }
                        }
                    },
                    "len": 103
                },
                "cols": {
                    "2": {
                        "width": 75
                    },
                    "3": {
                        "width": 108
                    },
                    "5": {
                        "width": 109
                    },
                    "6": {
                        "width": 108
                    },
                    "7": {
                        "width": 117
                    },
                    "len": 25
                },
                "validations": [],
                "autofilter": {
                    "ref": "E13",
                    "filters": [],
                    "sort": null
                }
            }
        ).change((cdata) => {
            // console.log(cdata);
        // console.log(xs.validate());
        var str = JSON.stringify(cdata)
        // console.log(str);
    });
    }
</script>
</html>