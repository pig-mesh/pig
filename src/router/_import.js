module.exports = (file, path) => {
  if (!path) path = 'page'
  return require(`../${path}/${file}.vue`)
}
