package io.bloznelis.aoc2020

import io.bloznelis.aoc2020.util.readFileLines

object Day3 {

  case class SlopeMap(rows: List[LazyList[Cell]])
  enum Cell {
    case Tree extends Cell
    case Empty extends Cell
  }

  def answers: (Int, Int) = {
    val map = makeMap()
    (countFirstPathTrees(map), countAllPathsTrees(map))
  }

  def makeMap(): SlopeMap = SlopeMap(
    readFileLines("data/input/day3.txt").map(line => LazyList.continually(parseLine(line)).flatten)
  )

  def parseLine(line: String): Vector[Cell] =
    line
      .toCharArray
      .map {
        case '.' => Cell.Empty
        case '#' => Cell.Tree
      }
      .toVector

  def countFirstPathTrees(slopeMap: SlopeMap): Int = {
    val firstPath = (0 until slopeMap.rows.size).map(rowIdx => (rowIdx * 3, rowIdx)).toVector
    countPathTrees(slopeMap, firstPath)
  }

  def countAllPathsTrees(slopeMap: SlopeMap): Int = {
    val range = (0 until slopeMap.rows.size)
    def count = countPathTrees(slopeMap, _)
    def toRight(by: Int) = range.map(rowIdx => (rowIdx * by, rowIdx))

    Vector(
      count(toRight(1)),
      count(toRight(3)),
      count(toRight(5)),
      count(toRight(7)),
      count(range.collect{ case rowIdx if rowIdx * 2 <= slopeMap.rows.size => (rowIdx, rowIdx * 2) })
    ).reduce(_ * _)
  }

  def countPathTrees(slopeMap: SlopeMap, path: Seq[(Int, Int)]): Int =
    path.map((x, y) => slopeMap.rows(y)(x)).filter(_ == Cell.Tree).size
}