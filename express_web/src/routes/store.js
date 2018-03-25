const express = require('express');
const router = express.Router();

const validTypes = [
    'weapons',
    'armour'
]

const view = 'store.html';

router.param(
    'type',
    function(req,res,next,type){
        if(!validTypes.includes(type)){
            res.redirect('/');
        }

        next();
    }
);

router.get('/',function(req,res){
    res.redirect('store/all');
});

router.get('/all',function(req,res){
    res.render('store');
});

router.get('/confirm',function(req,res){
    res.render('confirm-order',{});
});

router.get('/:type',function(req,res){
    const type = req.params.type;

    res.render('store',{

    });
});

module.exports = router;