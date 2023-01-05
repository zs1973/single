#将会在library module打包成aar时，以proguard.txt的形式存在，将参与到集成这个arr的App编译中，但不参与这个library
#module打包成aar的编译过程。
#配置方式:
# defaultConfig {
#    consumerProguardFiles "proguard-rules.pro"
# }