#!/usr/local/bin/ruby

# Запрашиваем Gem rasem для работы с svg
require 'rasem'

Epsilon = 0.00001

class Hex
	attr_accessor :img

	attr_accessor :radius
	attr_accessor :height

	attr_accessor :hole_radius
	attr_accessor :hole_height
	attr_accessor :lvl_number
	attr_accessor :hole_s

	def initialize
		@radius = 0
		@height = @radius * Cos30
	end

	# Функция установки значения для поля radius является длиной
	# радиуса описанной около правильного шестиугольника окружности
	def setr(r)
		@radius = r
		@height = @radius * Cos30

		@hole_radius = r/8
		@hole_height = @hole_radius * Cos30
		@lvl_number = 3
		@hole_s = @hole_radius
	end

	# Функция рисования шестиугольника строной radius и центром
	# в точке (x, y)

	# Вычисляет координаты вершин шестиугольника
	def calc(x, y, r)
		h = r * Cos30
		v =[[x, y - r],
			[x + h, y - r / 2],
			[x + h, y + r / 2],
			[x, y + r],
			[x - h, y + r / 2],
			[x - h, y - r / 2]]
	end

	# Вычисляет координаты вершин повернутого на 90 градусов шестиугольника
	def calc_rotated(x, y, r)
		h = r * Cos30
		v =[[x - r / 2, y - h],
			[x + r / 2, y - h],
			[x + r, y],
			[x + r / 2, y + h],
			[x - r / 2, y + h],
			[x - r, y]]
	end

	# Вычисляет расстояние между точками
	def dist(x1, y1, x2, y2)
		Math.sqrt( (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2) )
	end

	# Равномерно рисует шестиугольники на отрезке с концами (x1, y1), (x2, y2)
	def draw_on_line(x1, y1, x2, y2)
		if (x1 - x2).abs < Epsilon
			while dist(x1, y1, x2, y2) > @hole_radius do
				img.polygon *calc_rotated(x1, y1, @hole_radius), :stroke=>"black", :fill=>"black"

				y1 = y1 + (y1 < y2 ? 1: -1)*(2*@hole_height + @hole_s)
			end
		else
			while dist(x1, y1, x2, y2) > @hole_radius do
				img.polygon *calc_rotated(x1, y1, @hole_radius), :stroke=>"black", :fill=>"black"

				if y1 < y2 then
					y1 += @hole_height + @hole_s/2
				else
					y1 -= @hole_height + @hole_s/2
				end

				if x1 < x2 then
					x1 += @hole_radius + @hole_s + @hole_height/2
				else
					x1 -= @hole_radius + @hole_s + @hole_height/2
				end
			end
		end
	end

	def draw(img, x, y)
		@img = img
		img.polygon *calc(x, y, @radius), :stroke=>"rgb(15, 15, 15)", :fill=>"rgb(15, 15, 15)"

		radius = 0
		# Центры дырок находятся на вершинах невидимых шестиугольников
		@lvl_number.times do
			dots = calc(x, y, radius)

			if radius < Epsilon
				img.polygon *calc_rotated(*dots[0], @hole_radius), :stroke=>"black", :fill=>"black"
			end

			draw_on_line(*dots[0], *dots[1])
			draw_on_line(*dots[1], *dots[2])
			draw_on_line(*dots[2], *dots[3])
			draw_on_line(*dots[3], *dots[4])
			draw_on_line(*dots[4], *dots[5])
			draw_on_line(*dots[5], *dots[0])

			radius += @hole_s + @hole_height*2
		end
	end

	def draw_border(img, x, y, i)
		points = *calc(x, y, @radius*1.1)
		case i
		when 1; img.line *points[0], *points[1], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		when 2; img.line *points[1], *points[2], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		when 3; img.line *points[2], *points[3], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		when 4; img.line *points[3], *points[4], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		when 5; img.line *points[4], *points[5], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		when 6; img.line *points[5], *points[0], :stroke_width=>4, :stroke=>"rgb(255, 0, 255)"
		end
	end
end

# Приближенное значение косинуса 30 градусов
Cos30 = 0.87

# Расстояние между ближайшими точками двух соседних шестиугольников
$s = 2
$orien = 'X'

# Разрешение изображения
$resX = 1280
$resY = 700

# Требуемое кол-во шестиугольников по горизонтали
$hexX = 24

# Требуемое кол-во шестиугольников по вертикали
$hexY = 1

# Файл куда сохранится рисунок по умолчанию
$filename = "default.svg"

$hex = Hex.new

def recalc
	if $orien == 'X' then

		$hex.setr ($resX - $s*($hexX+1)) / ($hexX*2*Cos30)

		# Посчитать кол-во шестиугольников в стоблце
		$hexY = (($resY - $s - 0.5*$hex.radius) / ($s + 1.5 * $hex.radius)).to_i

		# Отсеч часть изображения куда не вместился ряд
		$resY = $s*($hexY+1) + 1.5*($hex.radius)*$hexY + 0.5*($hex.radius)
	else

		$hex.setr ($resY - $s*($hexY+1)) / (1.5*$hexY + 0.5)

		# Посчитать кол-во шестиугольников в строке
		$hexX = (($resX - $s) / ($s + 2*$hex.height)).to_i

		# Отсеч часть изображения куда не вместился столбец
		$resX = $s*($hexX+1) + 2*$hex.height*$hexX;
	end
end

def gen
	$img = Rasem::SVGImage.new($resX, $resY)

	shift = false
	y = $s + $hex.radius
	$hexX -= 1

	$hexY.times do
		x = $s + (shift ? 2*$hex.height + $s/2 : $hex.height);
		$hexX += (shift ? -1: +1);
		shift = !shift;

		$hexX.times do
			$hex.draw($img, x, y)
			x += $s + 2 * $hex.height
		end

		y += $s*Cos30 + 1.5 * $hex.radius
	end

	# рисуем горизонтальные стороны рамки
	x = $s + $hex.height;
	$hexX.times do
		$hex.draw_border($img, x, $hex.radius+$s, 1)
		$hex.draw_border($img, x, $hex.radius+$s, 6)
		$hex.draw_border($img, x + $hex.height, $resY-$s*4.5-$hex.radius, 3)
		$hex.draw_border($img, x + $hex.height, $resY-$s*4.5-$hex.radius, 4)
		x += $s + 2 * $hex.height
	end
	$hex.draw_border($img, x, $hex.radius+$s, 1)
	$hex.draw_border($img, x, $hex.radius+$s, 6)

	# рисуем вертикальные стороны рамки
	y = $s + $hex.radius
	shift = false
	$hexY.times do
		if shift then
			$hex.draw_border($img, 2*($s+$hex.height), y, 5)
		else
			$hex.draw_border($img, ($s+$hex.height), y, 4)
			$hex.draw_border($img, ($s+$hex.height), y, 5)
			$hex.draw_border($img, ($s+$hex.height), y, 6)
		end

		if shift then
			$hex.draw_border($img, $resX-2*($s+$hex.height), y, 2)
		else
			$hex.draw_border($img, $resX-($s+$hex.height), y, 1)
			$hex.draw_border($img, $resX-($s+$hex.height), y, 2)
			$hex.draw_border($img, $resX-($s+$hex.height), y, 3)
		end

		y += $s*Cos30 + 1.5 * $hex.radius
		shift = !shift
	end
end

def save
	$img.close
	File.open($filename, "w") do |file|
		file.puts $img.output
	end
end

#___________________________PREEXECUTABLE_COMMANDS_____________________________

recalc

#__________________________________MAIN________________________________________

while true
	print "> "
	str = gets

	case str.split(' ')[0]
	when /^[Hh][Ee][Ll][Pp]/
		puts "|"
		puts "| gen"
		puts "| resolution <Xpixel> <Ypixel>"
		puts "|"
		puts "| X <n> - set number of hexes in a column, Y counts automatically"
		puts "| Y <n> - set number of hexes in a row, X counts automatically"
		puts "|"
		puts "| hole_s <pixel> - distance between holes"
		puts "| hole_r <pixel> - length of side of the hole"
		puts "| lvl <number> - number of levels"
		puts "|"
		puts "| set_s <pixel>"
		puts "|"
		puts "| set_output_file <path>"
		puts "| wtf - show all parameters"
		puts "| toggle_orien"
		puts "|"
		puts "| exit"
		puts ""

	when "toggle_orien"
		$orien = ($orien == 'X' ? 'Y': 'X');

	when "gen"
		recalc
		gen
		save

	when "resolution"
		$resX, $resY = str.split(' ')[1].to_i, str.split(' ')[2].to_i

	when "set_s"
		$s = str.split(' ')[1].to_i

	when "set_output_file"
		$filename = str.split(' ')[1]

	when "X"
		$hexX = str.split(' ')[1].to_i
		recalc

	when "Y"
		$hexY = str.split(' ')[1].to_i
		recalc

	# Do we really need it?
	#when "lvl"
		#$hex.lvl_number = str.split(' ')[1].to_i

	#when "hole_s"
		#$hex.hole_s = str.split(' ')[1].to_i

	#when "hole_r"
		#$hex.hole_radius = str.split(' ')[1].to_i

	when /^[Ww][Tt][Ff]/
		puts "|"
		puts "| resolution: " + $resX.to_s + "x" + $resY.to_s
		puts "| hexolution: " + $hexX.to_s + "x" + $hexY.to_s
		puts "| dist between hexes: " + $s.to_s
		puts "| radius: " + $hex.radius.to_s
		puts "| output file name: " + $filename
		puts "| orien: " + $orien
		puts "| (hole_s, hole_r, lvl_number) = (#{$hex.hole_s}, #{$hex.hole_radius}, #{$hex.lvl_number})"
		puts ""

	when /^[Ee][Xx][Ii][Tt]/
		break
	end
end

