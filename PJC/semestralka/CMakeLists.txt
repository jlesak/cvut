cmake_minimum_required(VERSION 3.9)
project(semestralka)

set(CMAKE_CXX_STANDARD 11)

add_executable(semestralka main.cpp dijkstra.cpp dijkstra.hpp vertex.hpp edge.hpp)