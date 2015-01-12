--
-- Custom Writer for Google Code Wiki Syntax
--         https://code.google.com/p/support/wiki/WikiSyntax
--
-- Invoke with: pandoc -t GoogleWiki.lua

-- Escaping (one word at a time)
local function escape(s)
  return string.gsub(s, '([A-Z][a-z][a-z0-9]*[A-Z][a-z][A-Za-z0-9]*)', '!%1')
end

-- Blocksep is used to separate block elements.
function Blocksep()
  return "\n\n"
end

-- This function is called once for the whole document. Parameters:
-- body is a string, metadata is a table, variables is a table.
-- This gives you a fragment.  You could use the metadata table to
-- fill variables in a custom lua template.  Or, pass `--template=...`
-- to pandoc, and pandoc will add do the template processing as
-- usual.
-- TODO: add #summary, #labels, #sidebar
function Doc(body, metadata, variables)
  return body .. LineBreak()
end

-- The functions that follow render corresponding pandoc elements.
-- s is always a string, attr is always a table of attributes, and
-- items is always an array of strings (the items in a list).
-- Comments indicate the types of other variables.

function Str(s)
  return escape(s)
end

function Space()
  return " "
end

function LineBreak()
  return '\n'
end

function Emph(s)
  return "_" .. s .. "_"
end

function Strong(s)
  return "*" .. s .. "*"
end

function Subscript(s)
  return ",," .. s .. ",,"
end

function Superscript(s)
  return "^" .. s .. "^"
end

function SmallCaps(s)
  return s
end

function Strikeout(s)
  return '~~' .. s .. '~~'
end

function Link(s, src, tit)
  if s then
    return '[' .. src .. " " .. s .. ']'
  else
    return src
  end
end

function Image(s, src, tit)
  return src
end

function Code(s, attr)
  return '`' .. escape(s) .. '`'
end

function InlineMath(s)
  return escape(s) 
end

function DisplayMath(s)
  return escape(s) 
end

function Span(s, attr)
  return s
end

function Plain(s)
  return s
end

function Para(s)
  return s
end

-- lev is an integer, the header level.
function Header(level, s, attr)
  return string.rep("=", level) .. " " .. s .. " " .. string.rep("=", level)
end


function BlockQuote(s)
  -- should do this by increasing the indentation
  return s
end

function HorizontalRule()
  return LineBreak() .. "-----" .. LineBreak()
end

function CodeBlock(s, attr)
  return '{{{' .. LineBreak() .. escape(s) .. LineBreak() .. '}}}'
end

local function makelist(items, ltype)
  local buf = {}
  for _,e in ipairs(items) do
    table.insert(buf, " " .. ltype .. " " .. e )
  end
  return table.concat(buf, '\n')
end

function BulletList(items)
  return makelist(items, '*')
end

function OrderedList(items)
  return makelist(items, '#')
end

-- Convert pandoc alignment to something HTML can use.
-- align is AlignLeft, AlignRight, AlignCenter, or AlignDefault.
function html_align(align)
  if align == 'AlignLeft' then
    return 'left'
  elseif align == 'AlignRight' then
    return 'right'
  elseif align == 'AlignCenter' then
    return 'center'
  else
    return 'left'
  end
end

-- Caption is a string, aligns is an array of strings,
-- widths is an array of floats, headers is an array of
-- strings, rows is an array of arrays of strings.
function Table(cap, align, widths, headers, rows)
  local buf = {}
  for _,r in ipairs(rows) do
    local rbuf = '|| '
    for i,c in ipairs(r) do
      rbuf = rbuf .. c .. ' || '
    end
    table.insert(buf, rbuf)
  end
  return table.concat(buf, '\n')
end

function Div(s, attr)
  return s
end

-- The following code will produce runtime warnings when you haven't defined
-- all of the functions you need for the custom writer, so it's useful
-- to include when you're working on a writer.
local meta = {}
meta.__index =
  function(_, key)
    io.stderr:write(string.format("WARNING: Undefined function '%s'\n",key))
    return function() return "" end
  end
setmetatable(_G, meta)

