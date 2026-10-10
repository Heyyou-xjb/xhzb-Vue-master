import request from '@/utils/request'

// 查询护理项目列表
export function listNursing_project(query) {
  return request({
    url: '/nursing/nursing_project/list',
    method: 'get',
    params: query
  })
}

// 查询护理项目详细
export function getNursing_project(id) {
  return request({
    url: '/nursing/nursing_project/' + id,
    method: 'get'
  })
}

// 新增护理项目
export function addNursing_project(data) {
  return request({
    url: '/nursing/nursing_project',
    method: 'post',
    data: data
  })
}

// 修改护理项目
export function updateNursing_project(data) {
  return request({
    url: '/nursing/nursing_project',
    method: 'put',
    data: data
  })
}

// 删除护理项目
export function delNursing_project(id) {
  return request({
    url: '/nursing/nursing_project/' + id,
    method: 'delete'
  })
}
