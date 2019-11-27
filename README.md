为没有设置screenOrientation的activity设置

使用说明的话就像在app里面那样使用就完了

dontSetActivityNames表示不想统一设置的activity

screenOrientation用设置你想设置的方向



apply plugin: OrientationPlugin
testSet {
    dontSetActivityNames = ['com.yc.uniformsetorientationplugin.MainActivity']
    screenOrientation "portrait"
}

就这样哇!!!!
