cmake_minimum_required(VERSION 3.4.1)

file( GLOB app_src_files
     "src/main/cpp/*.cpp"
     "src/main/cpp/*.cc" )

add_library( pixel-proc SHARED ${app_src_files} )

find_library( log-lib log )
find_library( graphics-lib jnigraphics )

target_link_libraries( pixel-proc ${log-lib} ${graphics-lib} )