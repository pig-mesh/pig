<!-- 下拉输入框 支持下拉框、输入框切换 -->
<style>
    .jimu-cust-component .ivu-input-group{
        top: 0;
    }
</style>
<script type="text/x-template" id="select-input-template">
<div class="jimu-cust-component">
    <i-select v-if="selectFlag" filterable size="small" :value="selectedValue" :transfer="true" :clearable="true" @on-change="selectChange">
        <i-option v-for="(item, index) in selectOptions" :value="item.value" :key="index">{{ item.title }}</i-option>
    </i-select>
    <i-input v-else size="small" v-model="inputValue" @on-change="inputChange" placeholder="请输入自定义表达式">
        <i-button size="small" slot="append" icon="arrow-return-left" @click="backSelect">返回下拉框</i-button>
    </i-input>
</div>
</script>
<script>
    Vue.component('j-select-input', {
        template: '#select-input-template',
        props: {
            options: {
                type: Object,
                required: false,
                default:()=>{}
            },
            value:{
                type: String,
                required: false,
                default: ''
            }
        },
        data(){
            return {
                selectFlag: true,
                selectedValue: '',
                inputValue: '',
                selectOptions: []
            }
        },
        watch:{
            options:{
                deep: true,
                immediate: true,
                handler: function(){
                    this.resetOptions()
                }
            },
            value:{
                immediate: true,
                handler: function(){
                    if(!this.value){
                        this.selectFlag = true
                        this.selectedValue = ''
                        this.inputValue = ''
                    }else{
                        if(this.value.startsWith('=')){
                            this.selectFlag = false
                            this.selectedValue = ''
                            this.inputValue = this.value
                        }else{
                            this.selectFlag = true
                            this.selectedValue = this.value
                            this.inputValue = ''
                        }
                    }
                }
            }
        },
        methods:{
            // 清空
            resetOptions(){
                let arr = []
                for(let item of this.options){
                    arr.push({
                        title: item.title,
                        value: item.value
                    })
                }
                arr.push({
                    title: '自定义表达式',
                    value: '-1'
                })
                this.selectOptions = [...arr]
            },
            inputChange(e){
                this.$emit('on-change', e.target.value)
            },
            selectChange(value){
                if(value == -1){
                    this.selectFlag = false
                    this.selectedValue = ''
                    this.inputValue = ''
                }else{
                    this.selectedValue = value
                    this.$emit('on-change', this.selectedValue)
                }
            },
            backSelect(){
                this.selectFlag = true
            }


        }
    })
</script>